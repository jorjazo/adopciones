package cl.adopciones.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cl.adopciones.items.Item;
import cl.adopciones.items.ItemService;
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
	private ItemService itemService;
	
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
	
	@PostMapping("")
	public String createItem(@Valid ItemForm form, Model model)
	{
		Item newItem = form.toItem();
		newItem = itemService.save(newItem);
		
		return "redirect:" + getItemUrl(newItem);
	}

	@GetMapping("/{itemId}")
	public String displayItem(@PathVariable Long itemId, Model model) {
		Item item = itemService.getItem(itemId);
		model.addAttribute("item", item);
		
		return "items/display";
	}
	
	private String getItemUrl(Item newItem) {
		return "/items/" + newItem.getId();
	}
}
