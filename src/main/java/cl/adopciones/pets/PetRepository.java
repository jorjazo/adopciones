package cl.adopciones.pets;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PetRepository extends PagingAndSortingRepository<Pet, Long>, JpaSpecificationExecutor<Pet> {

	public Page<Pet> findAll(Example<Pet> example, Pageable page);

}
