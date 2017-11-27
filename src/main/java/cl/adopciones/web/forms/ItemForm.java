package cl.adopciones.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import cl.adopciones.items.Item;
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
	
	
	public Item toItem() {
		Item item = new Item();
		item.setPetName(petName);
		item.setPetType(getPetType());
		item.setPetAgeCategory(getPetAgeCategory());
		return item;
	}
}
