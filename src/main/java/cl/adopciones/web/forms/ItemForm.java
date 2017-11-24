package cl.adopciones.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.adopciones.items.PetAgeCategory;
import cl.adopciones.items.PetType;
import lombok.Data;

@Data
public class ItemForm {
	@Size(min=2,max=100)
	@NotNull
	private String petName;
	
	@NotNull
	private PetType petType;
	
	@NotNull
	private PetAgeCategory petAgeCategory;
	
}
