package cl.adopciones.pets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.assertj.core.api.Assertions.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import cl.adopciones.users.UserService;
import io.rebelsouls.chile.Comuna;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("dev")
@SpringBootTest
public class PetServiceTest {

	@Autowired
	PetService petService;
	
	@Autowired
	UserService userService;
	
	@Test
	public void shouldHaveTwoPets() {
		assertNotNull(petService);
		Page<Pet> searchResult = petService.searchPets(
				null,
				PetType.values(),
				Gender.values(),
				PetAgeCategory.values(),
				PetSizeCategory.values(),
				null,
				null,
				null,
				new PageRequest(0, 20));
		
		assertNotNull(searchResult);
		assertEquals(2, searchResult.getNumberOfElements());
		assertEquals("Panzer", searchResult.getContent().get(0).getName());
	}
	
	@Test
	@DirtiesContext
	public void shouldSaveNewPet() {
		Pet pet = new Pet();
		pet.setAgeCategory(PetAgeCategory.OLDER_THAN_SIX_YEARS);
		pet.setDescription("Un lindo gatito");
		pet.setGender(Gender.FEMALE);
		pet.setLocation(Comuna.Puchuncaví);
		pet.setName("Misifús");
		pet.setOwner(userService.loadUserById(1L));
		pet.setSizeCategory(PetSizeCategory.S);
		pet.setType(PetType.CAT);
		
		petService.save(pet);
		
		Page<Pet> searchResult = petService.searchPets(
				null,
				new PetType[] {PetType.CAT},
				Gender.values(),
				PetAgeCategory.values(),
				PetSizeCategory.values(),
				null,
				null,
				null,
				new PageRequest(0, 20));
		assertThat(searchResult)
			.hasSize(1);
		
		Pet found = searchResult.getContent().get(0);
		pet.setId(found.getId());
		
		assertThat(pet).isEqualTo(found);
	}
}
