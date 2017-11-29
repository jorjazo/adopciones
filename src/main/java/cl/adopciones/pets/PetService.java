package cl.adopciones.pets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PetService {

	@Autowired
	private PetRepository petRepository;

	public Pet save(Pet item) {
		return petRepository.save(item);
	}

	public Pet getItem(Long itemId) {
		return petRepository.findOne(itemId);
	}

	public Page<Pet> searchPets(String name, PetType petType, Gender gender, PetAgeCategory petAgeCategory,
			PetSizeCategory petSizeCategory, Pageable page) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setAgeCategory(petAgeCategory);
		pet.setGender(gender);
		pet.setSizeCategory(petSizeCategory);
		pet.setType(petType);
		Example<Pet> example = Example.of(pet);
		return petRepository.findAll(example, page);
	}
}
