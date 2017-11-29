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
import cl.adopciones.pets.PetService;
import cl.adopciones.web.forms.PetForm;

@Controller
@RequestMapping("/mascotas")
public class PetsController {

	@Autowired
	private PetService petService;

	@GetMapping("new")
	public String newItemForm(@ModelAttribute PetForm form, Model model) {
		return "pets/new";
	}

	@PostMapping("")
	public String createItem(@Valid PetForm form, Model model) {
		Pet newItem = form.toItem();
		newItem = petService.save(newItem);

		return "redirect:" + getItemUrl(newItem);
	}

	@GetMapping("/{petId}")
	public String displayItem(@PathVariable("petId") Pet pet, Model model) {
		model.addAttribute("pet", pet);
		return "pets/display";
	}

	private String getItemUrl(Pet newItem) {
		return "/pets/" + newItem.getId();
	}
}
