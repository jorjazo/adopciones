package cl.adopciones.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "io.rebelsouls.repositories", "cl.adopciones" })
@EntityScan(basePackages = { "io.rebelsouls.entities", "cl.adopciones" })
public class JpaConfig {

}
