package com.shri;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OffsetResetKafkaApp {

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(OffsetResetKafkaApp.class, args);
        ctx.getBean(OffsetResetKafka.class).setOffset();
    }
}
