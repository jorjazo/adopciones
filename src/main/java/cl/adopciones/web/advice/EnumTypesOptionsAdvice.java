package cl.adopciones.web.advice;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import cl.adopciones.pets.Gender;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetSizeCategory;
import cl.adopciones.pets.PetType;

@ControllerAdvice
public class EnumTypesOptionsAdvice {

	@ModelAttribute
	public void addPetEnumTypesOptions(Model model) {
		model.addAttribute("petTypes", PetType.values());
		model.addAttribute("petAgeCategories", PetAgeCategory.values());
		model.addAttribute("petSizeCategories", PetSizeCategory.values());
		model.addAttribute("genders", Gender.values());
	}
}
