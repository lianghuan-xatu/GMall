package com.xatu.gmall.controller;


import com.xatu.gmall.annotations.LoginRequired;
import com.xatu.gmall.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.List;

@Controller
public class SeckillController {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedissonClient redissonClient;

        @RequestMapping("/kill")
        @ResponseBody
        @LoginRequired(loginSuccess = true)
        public String kill(){
            String memberId = "1";
            Jedis jedis = redisUtil.getJedis();
            //开启商品监控
            jedis.watch("106");
            if(Integer.parseInt(jedis.get("106"))>0){
                Transaction multi = jedis.multi();
                jedis.incrBy("106", -1);
                List<Object> exec = multi.exec();
                if(exec!=null&&exec.size()>0){
                    System.out.println("成功");
                    //用消息队列发送订单消息
                }else{
                    System.out.println("失败");
                }
        }
            jedis.close();
           return "success";
}

    /**
     * 使用Redission
     * @return
     */
    @RequestMapping("/kill2")
    @ResponseBody
    @LoginRequired(loginSuccess = true)
    public String kill2(){
        String memberId = "1";
        Jedis jedis = redisUtil.getJedis();
        //开启商品监控
        RSemaphore semaphore = redissonClient.getSemaphore("106");
        boolean b = semaphore.tryAcquire();
        if(b){
                System.out.println("成功");
                //用消息队列发送订单消息
            }else{
                System.out.println("失败");
            }
        jedis.close();
        return "success";
    }

}
