package io.rebelsouls.template.services;

import java.util.Locale;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import io.rebelsouls.core.users.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender mailSender;

    public void emailUser(User user, String subject, String templateName, Map<String, Object> model) {
        Context context = new Context(Locale.getDefault(), model);
        model.put("mailSubject", subject);
        String result = templateEngine.process(templateName, context);

        MimeMessage email = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email);
        try {
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(result, true);
        } catch (MessagingException e) {
            log.error("Could not send email", e);
        }
        mailSender.send(email);
    }

}
