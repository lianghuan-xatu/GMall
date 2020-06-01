package com.xatu.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GmallOrderWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallOrderWebApplication.class, args);
    }

}
