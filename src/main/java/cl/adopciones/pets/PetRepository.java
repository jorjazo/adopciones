package cl.adopciones.pets;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PetRepository extends PagingAndSortingRepository<Pet, Long> {

	@Query("Select p from Pet p where (?1 is null or p.name = ?1) and (?2 is null or p.type = ?2) and (?3 is null or p.gender = ?3) and (?4 is null or p.ageCategory = ?4) and (?5 is null or p.sizeCategory = ?5)")
	public Page<Pet> search(String name, PetType petType, Gender gender, PetAgeCategory petAgeCategory,
			PetSizeCategory petSizeCategory, Pageable page);
	
	public Page<Pet> findAll(Example<Pet> example, Pageable page);

}
