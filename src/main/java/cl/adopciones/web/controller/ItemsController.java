package cl.adopciones.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cl.adopciones.items.PetAgeCategory;
import cl.adopciones.items.PetType;
import cl.adopciones.users.UserService;
import cl.adopciones.web.forms.ItemForm;
import io.rebelsouls.email.EmailService;
import io.rebelsouls.util.RecaptchaVerifier;

@Controller
@RequestMapping("/items")
public class ItemsController {

	@Autowired
	private UserService userService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private RecaptchaVerifier recaptchaVerifier;

	@Value("${app.url}")
	private String appUrl;

	@Value("${app.recaptcha.key}")
	private String recaptchaKey;


	@GetMapping("new")
	public String newItemForm(@ModelAttribute ItemForm form, Model model) {
		model.addAttribute("petTypes", PetType.values());
		model.addAttribute("petAgeCategories", PetAgeCategory.values());
		return "items/new";
	}
	
	
}
