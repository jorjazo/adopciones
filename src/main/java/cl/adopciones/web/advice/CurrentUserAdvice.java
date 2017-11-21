package cl.adopciones.web.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.rebelsouls.entities.User;

@ControllerAdvice
public class CurrentUserAdvice {

	@ModelAttribute("currentUser")
	public User getCurrentUser(@AuthenticationPrincipal User user) {
		return user;
	}
	
}
