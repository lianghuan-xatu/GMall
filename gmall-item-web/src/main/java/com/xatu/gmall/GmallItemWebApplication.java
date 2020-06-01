package com.xatu.gmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class GmallItemWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(GmallItemWebApplication.class, args);
    }

}
