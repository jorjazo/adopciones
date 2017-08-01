package io.rebelsouls.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"io.rebelsouls.services", "io.rebelsouls.spring.config", "io.rebelsouls.template"})
public class TemplateApp {

    public static void main(String[] args) {
        SpringApplication.run(TemplateApp.class, args);
    }

}
