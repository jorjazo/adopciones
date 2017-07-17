package io.rebelsouls.template.services;

import java.io.ByteArrayOutputStream;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.jtwig.spring.JtwigViewResolver;
import org.jtwig.web.servlet.JtwigDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import io.rebelsouls.core.users.User;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Autowired
	private JtwigViewResolver jtwigViewResolver;
	
	@Autowired 
	private JavaMailSender mailSender;

	public void emailUser(User user, String subject, String templateResource, Map<String, Object> model) {

		JtwigDispatcher view = jtwigViewResolver.getRenderer().dispatcherFor("classpath:/templates/users/registration-email.twig");
		view.with(model);
		view.with("mailSubject", subject);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		view.render(baos);
		String result = new String(baos.toByteArray());

		MimeMessage email = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(email);
		try {
			helper.setTo(user.getEmail());
			helper.setSubject(subject);
			helper.setText(result);
			helper.setText(result, true);
		} catch (MessagingException e) {
			log.error("Could not send email", e);
		}
		mailSender.send(email);
	}
	
}
