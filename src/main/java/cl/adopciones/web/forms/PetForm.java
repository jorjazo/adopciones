package cl.adopciones.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.adopciones.pets.Gender;
import cl.adopciones.pets.Pet;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetSizeCategory;
import cl.adopciones.pets.PetType;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;
import lombok.Data;

@Data
public class PetForm {
	@Size(min=2,max=100)
	@NotNull
	private String name;
	
	@NotNull
	private PetType type;
	
	@NotNull
	private Gender gender;

	@NotNull
	private PetAgeCategory ageCategory;
	
	@NotNull
	private PetSizeCategory sizeCategory;
	
	@Size(min=0,max=1024)
	private String description;
	
	@NotNull private Region region;
	@NotNull private Provincia provincia;
	@NotNull private Comuna comuna;
	
	public Pet toItem() {
		Pet pet = new Pet();
		pet.setName(getName());
		pet.setType(getType());
		pet.setGender(getGender());
		pet.setAgeCategory(getAgeCategory());
		pet.setSizeCategory(getSizeCategory());
		pet.setDescription(getDescription());
		pet.setLocation(getComuna());
		return pet;
	}
}
