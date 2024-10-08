package com.miapp.reportes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = "com.miapp.sistemasdistribuidos.entity")
@SpringBootApplication
public class ReportesApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReportesApplication.class, args);
    }

}
