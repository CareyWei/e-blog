package com.dyzzw.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.dyzzw.blog.mapper ")
public class CBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(CBlogApplication.class, args);

        System.setProperty("es.set.netty.runtime.avaliable.processors","false");
        System.out.println("http://localhost:8080/");
    }

}
