package io.rebelsouls.template.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = { "io.rebelsouls.repositories" })
@EntityScan(basePackages = { "io.rebelsouls.entities" })
public class JpaConfig {

}
