package org.example.notifyservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class NotifyServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotifyServiceApplication.class, args);
    }

}
