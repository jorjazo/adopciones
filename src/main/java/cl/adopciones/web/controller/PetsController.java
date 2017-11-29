package cl.adopciones.web.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cl.adopciones.pets.Pet;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetService;
import cl.adopciones.pets.PetType;
import cl.adopciones.web.forms.PetForm;

@Controller
@RequestMapping("/mascotas")
public class PetsController {

	@Autowired
	private PetService itemService;

	@GetMapping("new")
	public String newItemForm(@ModelAttribute PetForm form, Model model) {
		model.addAttribute("petTypes", PetType.values());
		model.addAttribute("petAgeCategories", PetAgeCategory.values());
		return "pets/new";
	}

	@PostMapping("")
	public String createItem(@Valid PetForm form, Model model) {
		Pet newItem = form.toItem();
		newItem = itemService.save(newItem);

		return "redirect:" + getItemUrl(newItem);
	}

	@GetMapping("/{itemId}")
	public String displayItem(@PathVariable Long itemId, Model model) {
		Pet item = itemService.getItem(itemId);
		model.addAttribute("item", item);

		return "pets/display";
	}

	private String getItemUrl(Pet newItem) {
		return "/pets/" + newItem.getId();
	}
}
