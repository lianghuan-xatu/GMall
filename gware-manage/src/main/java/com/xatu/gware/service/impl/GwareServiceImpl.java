package com.xatu.gware.service.impl;


import com.alibaba.fastjson.JSON;

import com.xatu.gware.util.ActiveMQUtil;
import com.xatu.gware.util.HttpclientUtil;
import com.xatu.gware.bean.WareInfo;
import com.xatu.gware.bean.WareOrderTask;
import com.xatu.gware.bean.WareOrderTaskDetail;
import com.xatu.gware.bean.WareSku;
import com.xatu.gware.enums.TaskStatus;
import com.xatu.gware.mapper.WareInfoMapper;
import com.xatu.gware.mapper.WareOrderTaskDetailMapper;
import com.xatu.gware.mapper.WareOrderTaskMapper;
import com.xatu.gware.mapper.WareSkuMapper;
import com.xatu.gware.service.GwareService;
import org.apache.activemq.command.ActiveMQMapMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.*;



@Service
public class GwareServiceImpl implements GwareService {

     @Autowired
     WareSkuMapper wareSkuMapper;

    @Autowired
    WareInfoMapper wareInfoMapper;

    @Autowired
    WareOrderTaskMapper wareOrderTaskMapper;

    @Autowired
    WareOrderTaskDetailMapper wareOrderTaskDetailMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Value("${order.split.url}")
    private String ORDER_URL;

     public Integer  getStockBySkuId(String skuid){
         Integer stock = wareSkuMapper.selectStockBySkuid(skuid);

         return stock;
     }


    public boolean  hasStockBySkuId(String skuid,Integer num){
        Integer stock = getStockBySkuId(  skuid);

        if(stock==null||stock<num){
            return false;
        }
        return true;
    }


    public List<WareInfo> getWareInfoBySkuid(String skuid){
        List<WareInfo> wareInfos = wareInfoMapper.selectWareInfoBySkuid(skuid);
        return wareInfos;
    }

    public List<WareInfo> getWareInfoList(){
        List<WareInfo> wareInfos = wareInfoMapper.selectAll();
        return wareInfos;
    }


    public void addWareInfo(){
        WareInfo wareInfo =new WareInfo();
        wareInfo.setAddress("1123");
        wareInfo.setAreacode("123123");
        wareInfo.setName("123123");
        wareInfoMapper.insertSelective(wareInfo);


        WareSku wareSku =new WareSku();
        wareSku.setId(wareInfo.getId());
        wareSku.setWarehouseId("991");
        wareSkuMapper.insertSelective(wareSku);
    }


    public   Map<String,List<String>>  getWareSkuMap(List<String> skuIdlist){
        Example example=new Example(WareSku.class);
        example.createCriteria().andIn("skuId",skuIdlist);
        List<WareSku> wareSkuList = wareSkuMapper.selectByExample(example);

        Map<String,List<String>> wareSkuMap=new HashMap<>();

        for (WareSku wareSku : wareSkuList) {
            List<String>  skulistOfWare = wareSkuMap.get(wareSku.getWarehouseId());
            if (skulistOfWare==null){
                skulistOfWare=new ArrayList<>();
            }
            skulistOfWare.add(wareSku.getSkuId());
            wareSkuMap.put(wareSku.getWarehouseId(),skulistOfWare);
        }

        return  wareSkuMap;

    }


    public  List<Map<String,Object>>  convertWareSkuMapList( Map<String,List<String>>  wareSkuMap){

        List<Map<String,Object>> wareSkuMapList=new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : wareSkuMap.entrySet()) {
            Map<String,Object> skuWareMap=new HashMap<>();
            String wareid= entry.getKey();
            skuWareMap.put("wareId",wareid);
            List<String> skuids = entry.getValue();
            skuWareMap.put("skuIds",skuids);
            wareSkuMapList.add(skuWareMap);
        }
        return wareSkuMapList;

    }


    public void addWareSku(WareSku wareSku){
        wareSkuMapper.insertSelective(wareSku);
    }

    public List<WareSku> getWareSkuList(){
        List<WareSku> wareSkuList = wareSkuMapper.selectWareSkuAll();
        return wareSkuList;
    }

    public WareOrderTask getWareOrderTask(String taskId){

        WareOrderTask wareOrderTask  = wareOrderTaskMapper.selectByPrimaryKey(taskId);

        WareOrderTaskDetail wareOrderTaskDetail=new WareOrderTaskDetail();
        wareOrderTaskDetail.setTaskId(taskId);
        List<WareOrderTaskDetail> details = wareOrderTaskDetailMapper.select(wareOrderTaskDetail);
        wareOrderTask.setDetails(details);
        return wareOrderTask;
    }




    /***
     * 出库操作  减库存和锁定库存，
     * @param taskExample
     */
    @Transactional
    public void deliveryStock(WareOrderTask  taskExample)  {
        String trackingNo = taskExample.getTrackingNo();
        WareOrderTask wareOrderTask=getWareOrderTask(  taskExample.getId());
        wareOrderTask.setTaskStatus(TaskStatus.DELEVERED);
        List<WareOrderTaskDetail> details = wareOrderTask.getDetails();
        for (WareOrderTaskDetail detail : details) {
                WareSku wareSku=new WareSku();
                wareSku.setWarehouseId(wareOrderTask.getWareId());
                wareSku.setSkuId(detail.getSkuId());
                wareSku.setStock(detail.getSkuNum());
                wareSkuMapper.deliveryStock(wareSku);
        }

        wareOrderTask.setTaskStatus(TaskStatus.DELEVERED);
        wareOrderTask.setTrackingNo(trackingNo);
        wareOrderTaskMapper.updateByPrimaryKeySelective(wareOrderTask);
        try {
            sendToOrder(wareOrderTask);
        } catch(JMSException e){
            e.printStackTrace();
        }
    }


    public void sendToOrder(WareOrderTask wareOrderTask) throws JMSException{
            Connection conn = activeMQUtil.getConnection();

            Session session = conn.createSession(true, Session.SESSION_TRANSACTED);
            Destination destination = session.createQueue("SKU_DELIVER_QUEUE");
            MessageProducer producer = session.createProducer(destination);
            MapMessage mapMessage=new ActiveMQMapMessage();
            mapMessage.setString("orderId",wareOrderTask.getOrderId());
            mapMessage.setString("status",wareOrderTask.getTaskStatus().toString()); //小细节 枚举
            mapMessage.setString("trackingNo",wareOrderTask.getTrackingNo());

            producer.send(mapMessage);
            session.commit();

    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public  List<WareOrderTask>   checkOrderSplit(WareOrderTask wareOrderTask) {
        List<WareOrderTaskDetail> details = wareOrderTask.getDetails();
        List<String> skulist = new ArrayList<>();
        for (WareOrderTaskDetail detail : details) {
            skulist.add(detail.getSkuId());
        }
        Map<String, List<String>> wareSkuMap = getWareSkuMap(skulist);
        // 一次物流运输无法完成订单
        if (wareSkuMap.entrySet().size()<2) {
            Map.Entry<String, List<String>> entry = wareSkuMap.entrySet().iterator().next();
            String wareid = entry.getKey();
            wareOrderTask.setWareId(wareid);
        } else {
            //需要拆单
            List<Map<String, Object>> wareSkuMapList = convertWareSkuMapList(wareSkuMap);
            String jsonString = JSON.toJSONString(wareSkuMapList);
            Map<String, String> map = new HashMap<>();
            map.put("orderId", wareOrderTask.getOrderId());
            map.put("wareSkuMap", jsonString);
            
            // 调用订单系统拆单接口
            String resultJson = HttpclientUtil.doPost(  ORDER_URL, map);

            List<WareOrderTask> wareOrderTaskList = JSON.parseArray(resultJson, WareOrderTask.class);
            
            if(wareOrderTaskList==null){
                wareOrderTaskList = new ArrayList<>();
                for (WareOrderTaskDetail detail : details) {
                    WareOrderTask wareOrderTask1 = new WareOrderTask();

                    List<WareOrderTaskDetail> wareOrderTaskDetails = new ArrayList<>();
                    wareOrderTaskDetails.add(detail);
                    wareOrderTask1.setDetails(wareOrderTaskDetails);
                    String skuId = detail.getSkuId();
                    WareSku wareSku = new WareSku();
                    wareSku.setSkuId(skuId);
                    wareSku.setStock(null);
                    List<WareSku> select = wareSkuMapper.select(wareSku);
                    wareOrderTask1.setWareId(select.get(0).getWarehouseId());

                    wareOrderTaskList.add(wareOrderTask1);
                }
            }
            
            
            if (wareOrderTaskList.size()>=2){
//                for (WareOrderTask subOrderTask : wareOrderTaskList) {
//                    subOrderTask.setTaskStatus(TaskStatus.DEDUCTED);
//                    saveWareOrderTask(subOrderTask);
//                }
//                updateStatusWareOrderTaskByOrderId(wareOrderTask.getOrderId(),TaskStatus.SPLIT);
                return wareOrderTaskList;
            }else{
                throw new  RuntimeException("拆单异常!!");
            }

        }

        return  null;
    }


        public WareOrderTask saveWareOrderTask(WareOrderTask wareOrderTask ){
            wareOrderTask.setCreateTime(new Date());
            WareOrderTask wareOrderTaskQuery=new WareOrderTask();
            wareOrderTaskQuery.setOrderId(wareOrderTask.getOrderId());

            WareOrderTask wareOrderTaskOrigin = wareOrderTaskMapper.selectOne(wareOrderTaskQuery);
            if(wareOrderTaskOrigin!=null){
                return  wareOrderTaskOrigin;
            }

            wareOrderTaskMapper.insert(wareOrderTask);

            List<WareOrderTaskDetail> wareOrderTaskDetails = wareOrderTask.getDetails();
            for (WareOrderTaskDetail wareOrderTaskDetail : wareOrderTaskDetails) {
                wareOrderTaskDetail.setTaskId(wareOrderTask.getId());
                wareOrderTaskDetailMapper.insert(wareOrderTaskDetail);
            }
            return wareOrderTask;

        }


        public void updateStatusWareOrderTaskByOrderId(String orderId,TaskStatus taskStatus){
            Example example=new Example(WareOrderTask.class);
            example.createCriteria().andEqualTo("orderId",orderId);
            WareOrderTask wareOrderTask=new WareOrderTask();
            wareOrderTask.setTaskStatus(taskStatus);
            wareOrderTaskMapper.updateByExampleSelective(wareOrderTask,example);
        }


    /***
     * 库存锁定成功，准备出库，发送消息，由订单系统消费，修改状态为商品准备出库
     * @param wareOrderTask
     * @throws JMSException
     */
    public void sendSkuDeductMQ(WareOrderTask wareOrderTask) throws JMSException{
        Connection conn = activeMQUtil.getConnection();

    Session session = conn.createSession(true, Session.SESSION_TRANSACTED);
    Destination destination = session.createQueue("SKU_DEDUCT_QUEUE");
    MessageProducer producer = session.createProducer(destination);
    MapMessage mapMessage=new ActiveMQMapMessage();
        mapMessage.setString("orderId",wareOrderTask.getOrderId());
        mapMessage.setString("status",wareOrderTask.getTaskStatus().toString());
        producer.send(mapMessage);
        session.commit();
}

    @Transactional
    public void lockStock(WareOrderTask wareOrderTask) {
        List<WareOrderTaskDetail> wareOrderTaskDetails = wareOrderTask.getDetails();
        String comment = "";
        for (WareOrderTaskDetail wareOrderTaskDetail : wareOrderTaskDetails) {

            WareSku wareSku = new WareSku();
            wareSku.setWarehouseId(wareOrderTask.getWareId());
            wareSku.setStockLocked(wareOrderTaskDetail.getSkuNum());
            wareSku.setSkuId(wareOrderTaskDetail.getSkuId());

            int availableStock = wareSkuMapper.selectStockBySkuidForUpdate(wareSku); //查询可用库存 加行级写锁 注意索引避免表锁
            if (availableStock - wareOrderTaskDetail.getSkuNum() < 0) {
                comment += "减库存异常：名称：" + wareOrderTaskDetail.getSkuName() + "，实际可用库存数" + availableStock + ",要求库存" + wareOrderTaskDetail.getSkuNum();
            }
        }

        if (comment.length() > 0) {   //库存超卖 记录日志，返回错误状态
            wareOrderTask.setTaskComment(comment);
            wareOrderTask.setTaskStatus(TaskStatus.OUT_OF_STOCK);
            updateStatusWareOrderTaskByOrderId(wareOrderTask.getOrderId(),TaskStatus.OUT_OF_STOCK);

        } else {   //库存正常  进行减库存
            for (WareOrderTaskDetail wareOrderTaskDetail : wareOrderTaskDetails) {

                WareSku wareSku = new WareSku();
                wareSku.setWarehouseId(wareOrderTask.getWareId());
                wareSku.setStockLocked(wareOrderTaskDetail.getSkuNum());
                wareSku.setSkuId(wareOrderTaskDetail.getSkuId());

                wareSkuMapper.incrStockLocked(wareSku); //  加行级写锁 注意索引避免表锁

            }
            wareOrderTask.setTaskStatus(TaskStatus.DEDUCTED);
            updateStatusWareOrderTaskByOrderId(wareOrderTask.getOrderId(),TaskStatus.DEDUCTED);

        }

        try {
            sendSkuDeductMQ(wareOrderTask);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return;

    }



    public List<WareOrderTask> getWareOrderTaskList(WareOrderTask wareOrderTask){
             List<WareOrderTask> wareOrderTasks=null;
            if(wareOrderTask==null){
                 wareOrderTasks = wareOrderTaskMapper.selectAll();
            }else{
                wareOrderTasks = wareOrderTaskMapper.select(wareOrderTask);
            }
            return wareOrderTasks;
    }

}
