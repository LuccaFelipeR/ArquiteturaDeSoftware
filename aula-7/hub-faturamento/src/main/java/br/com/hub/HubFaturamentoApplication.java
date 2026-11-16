package br.com.hub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HubFaturamentoApplication {
    public static void main(String[] args) {
        SpringApplication.run(HubFaturamentoApplication.class, args);
    }
}
