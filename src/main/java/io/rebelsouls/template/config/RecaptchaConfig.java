package io.rebelsouls.template.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.rebelsouls.util.RecaptchaVerifier;

@Configuration
public class RecaptchaConfig {

    @Autowired
    @Bean
    public RecaptchaVerifier buildRecaptchaVerifier(@Value("${app.recaptcha.secret}") String secret) {
        RecaptchaVerifier verifier = new RecaptchaVerifier(secret);
        return verifier;
    }
}
