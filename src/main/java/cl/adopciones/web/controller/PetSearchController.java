package cl.adopciones.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cl.adopciones.pets.Gender;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetService;
import cl.adopciones.pets.PetSizeCategory;
import cl.adopciones.pets.PetType;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;

@Controller
@RequestMapping("/busqueda")
public class PetSearchController {

	@Autowired
	private PetService petService;

	@GetMapping("")
	public String getAllItems(@RequestParam(name = "name", required = false) String name,
			@RequestParam(name = "type", required = false) PetType[] petType,
			@RequestParam(name = "gender", required = false) Gender[] gender,
			@RequestParam(name = "ageCategory", required = false) PetAgeCategory[] petAgeCategory,
			@RequestParam(name = "sizeCategory", required = false) PetSizeCategory[] petSizeCategory,
			@RequestParam(name = "region", required = false) Region region,
			@RequestParam(name = "provincia", required = false) Provincia provincia,
			@RequestParam(name = "comuna", required = false) Comuna comuna,
			Model model,
			Pageable page) {

		if ("".equals(name))
			name = null;

		model.addAttribute("searchResults",
				petService.searchPets(name, petType, gender, petAgeCategory, petSizeCategory, region, provincia, comuna, page));
		
		return "pets/search-results";
	}

}
