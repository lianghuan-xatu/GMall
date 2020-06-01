package com.xatu.gware.bean;

import com.xatu.gware.enums.TaskStatus;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @param
 * @return
 */
public class WareOrderTask {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id ;

    @Column
    private String orderId;

    @Column
    private String consignee;

    @Column
    private String consigneeTel;

    @Column
    private String deliveryAddress;

    @Column
    private String orderComment;

    @Column
    private String paymentWay;

    @Column
    @ColumnType(jdbcType = JdbcType.VARCHAR)
    private TaskStatus taskStatus;

    @Column
    private String orderBody;

    @Column
    private String trackingNo;

    @Column
    private Date createTime;

    @Column
    private String wareId;

    @Column
    private String taskComment;

    @Transient
    private List<WareOrderTaskDetail> details;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsigneeTel() {
        return consigneeTel;
    }

    public void setConsigneeTel(String consigneeTel) {
        this.consigneeTel = consigneeTel;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getOrderComment() {
        return orderComment;
    }

    public void setOrderComment(String orderComment) {
        this.orderComment = orderComment;
    }

    public String getPaymentWay() {
        return paymentWay;
    }

    public void setPaymentWay(String paymentWay) {
        this.paymentWay = paymentWay;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getOrderBody() {
        return orderBody;
    }

    public void setOrderBody(String orderBody) {
        this.orderBody = orderBody;
    }

    public String getTrackingNo() {
        return trackingNo;
    }

    public void setTrackingNo(String trackingNo) {
        this.trackingNo = trackingNo;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<WareOrderTaskDetail> getDetails() {
        return details;
    }



    public void setDetails(List<WareOrderTaskDetail> details) {
        this.details = details;
    }

    public String getWareId() {
        return wareId;
    }

    public void setWareId(String wareId) {
        this.wareId = wareId;
    }

    public String getTaskComment() {
        return taskComment;
    }

    public void setTaskComment(String taskComment) {
        this.taskComment = taskComment;
    }
}
