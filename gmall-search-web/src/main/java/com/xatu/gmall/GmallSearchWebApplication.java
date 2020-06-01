package com.xatu.gmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.xatu.gmall.service.SkuService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GmallSearchWebApplication {


    public static void main(String[] args) {
        SpringApplication.run(GmallSearchWebApplication.class, args);
    }

}
