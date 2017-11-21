package cl.adopciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages={"io.rebelsouls.services", "io.rebelsouls.spring.config", "cl.adopciones"})
public class AdopcionesApp {

    public static void main(String[] args) {
        SpringApplication.run(AdopcionesApp.class, args);
    }

}
