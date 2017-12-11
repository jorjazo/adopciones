package cl.adopciones.pets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("dev")
@SpringBootTest
public class PetServiceTest {

	@Autowired
	PetService petService;
	
	@Test
	public void shouldHaveTwoPets() {
		assertNotNull(petService);
		Page<Pet> searchResult = petService.searchPets(null, PetType.values(), Gender.values(), PetAgeCategory.values(), PetSizeCategory.values(), null, null, null, new PageRequest(0, 20));
		assertNotNull(searchResult);
		assertEquals(2, searchResult.getNumberOfElements());
		assertEquals("Panzer", searchResult.getContent().get(0).getName());
		
	}
}
