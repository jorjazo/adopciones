package cl.adopciones.web.advice;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cl.adopciones.users.User;

@ControllerAdvice
public class PetPermissionsAdvice {

	@ModelAttribute("canCreatePet")
	public boolean canCreatePet(@AuthenticationPrincipal User currentUser) {
		return currentUser != null && currentUser.getOrganization() != null;
	}
}
