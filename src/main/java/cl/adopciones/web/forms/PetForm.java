package cl.adopciones.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.adopciones.pets.Pet;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetType;
import lombok.Data;

@Data
public class PetForm {
	@Size(min=2,max=100)
	@NotNull
	private String petName;
	
	@NotNull
	private PetType petType;
	
	@NotNull
	private PetAgeCategory petAgeCategory;
	
	
	public Pet toItem() {
		Pet item = new Pet();
		item.setName(petName);
		item.setType(getPetType());
		item.setAgeCategory(getPetAgeCategory());
		return item;
	}
}
