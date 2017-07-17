package io.rebelsouls.template.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages="io.rebelsouls.template.repositories")
public class JpaConfig {

}
