package io.rebelsouls.integration.email;

import java.util.HashMap;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import io.rebelsouls.core.users.User;
import io.rebelsouls.template.TemplateApp;
import io.rebelsouls.template.services.EmailService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes=TemplateApp.class)
public class EmailTests {

    @Autowired EmailService emailService;
    
    @Test
    public void testEmailSending() {
        
        User user = new User();
        user.setEmail("name@company.com");
        
        String random = UUID.randomUUID().toString();
        
        emailService.emailUser(user, "Test " + random, "users/registration-email", new HashMap<>());
    }
}
