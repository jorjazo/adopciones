package cl.adopciones.web.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cl.adopciones.users.Role;
import cl.adopciones.users.User;

@ControllerAdvice
public class AdminPermissionsAdvice {

	@ModelAttribute("canManageUsers")
	public boolean canManageUsers(@AuthenticationPrincipal User currentUser) {
		return currentUser != null && currentUser.getRoles().contains(Role.ADMIN);
	}
}
