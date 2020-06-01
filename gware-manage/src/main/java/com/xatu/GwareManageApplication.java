package com.xatu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.xatu.gware")
@MapperScan(basePackages = "com.xatu.gware.mapper")
public class GwareManageApplication {

	public static void main(String[] args) {
		SpringApplication.run(GwareManageApplication.class, args);
	}
}
