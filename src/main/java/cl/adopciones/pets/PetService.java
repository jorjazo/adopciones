package cl.adopciones.pets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import static cl.adopciones.pets.PetSpecifications.*;
import static org.springframework.data.jpa.domain.Specifications.*;

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

	public Page<Pet> searchPets(String name, PetType[] petType, Gender[] gender, PetAgeCategory[] petAgeCategory,
			PetSizeCategory[] petSizeCategory, Pageable page) {

		return petRepository.findAll(where(hasName(name)).and(typeIn(petType)).and(genderIn(gender))
				.and(sizeIn(petSizeCategory)).and(ageIn(petAgeCategory)), page);
	}
}
