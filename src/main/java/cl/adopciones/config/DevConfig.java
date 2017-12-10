package cl.adopciones.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.thymeleaf.TemplateEngine;

@Configuration
@Profile("dev")
public class DevConfig {


	@Bean
	public TemplateEngine getTemplateEngine() {
		return new TemplateEngine();
	}
	
	@Bean
	public JavaMailSender getMailSender() {
		return new JavaMailSenderImpl();
	}
}
