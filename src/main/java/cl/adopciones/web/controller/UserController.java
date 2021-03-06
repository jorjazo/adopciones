package cl.adopciones.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cl.adopciones.users.Role;
import cl.adopciones.users.User;
import cl.adopciones.users.UserService;
import cl.adopciones.web.exceptions.InvalidRequestException;
import cl.adopciones.web.exceptions.ResourceNotFoundException;
import cl.adopciones.web.forms.UserRegistrationForm;
import io.rebelsouls.email.EmailService;

@Controller
@RequestMapping("/users")
//@Slf4j
public class UserController {

	private static final String USER_FORM_VIEW_NAME = "users/form";

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Value("${app.url}")
	private String appUrl;

	@GetMapping("")
	public String displayUserListAndForm(@AuthenticationPrincipal User currentUser, Model model,
			UserRegistrationForm userRegistrationForm) {
		if (currentUser == null)
			return USER_FORM_VIEW_NAME;

		if (currentUser.hasRole("ADMIN")) {
			model.addAttribute("userPage", userService.getPageOfUsers(0, 3));
			return "users/list";
		}

		return USER_FORM_VIEW_NAME;
	}

	@PostMapping("")
	public String registerUser(@AuthenticationPrincipal User currentUser,
			@Valid UserRegistrationForm userRegistrationForm, BindingResult bindingResult, Model model,
			HttpServletRequest request) {
		if (!userRegistrationForm.getPassword().equals(userRegistrationForm.getPasswordRepeat())) {
			bindingResult.rejectValue("passwordRepeat", "Debe coincidir con la contraseña ingresada");
		}

		if (bindingResult.hasErrors()) {
			return USER_FORM_VIEW_NAME;
		}

		User newUser = userService.createUser(userRegistrationForm.getEmail(), userRegistrationForm.getPassword(),
				userRegistrationForm.getName(), Role.USER);

		Map<String, Object> values = new HashMap<>();
		values.put("newUser", newUser);
		emailService.sendEmailAsync(newUser.getEmail(), "¡Bienvenido a Adopciones.cl!", "users/registration-email",
				values);

		return "users/create-success";
	}

	@GetMapping("/{userId}")
	@PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or principal.id == #userId)")
	public String userDetails(@PathVariable("userId") Long userId, @AuthenticationPrincipal User currentUser,
			Model model) {
		User user = userService.loadUserById(userId);

		if (user == null)
			throw new ResourceNotFoundException();

		model.addAttribute("user", user);
		if (user.getId().equals(currentUser.getId()))
			return "users/home";
		else if (currentUser.hasRole("ADMIN"))
			return "users/details";

		// Should not get here
		throw new InvalidRequestException();
	}

}
