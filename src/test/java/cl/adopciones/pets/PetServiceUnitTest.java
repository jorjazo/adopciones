package cl.adopciones.pets;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import cl.adopciones.organizations.Organization;
import cl.adopciones.users.User;

public class PetServiceUnitTest {

	@Test(expected=IllegalArgumentException.class)
	public void shouldNotSaveInexistentPet() {
		PetServiceImpl petService = new PetServiceImpl();
		Pet pet = new Pet();
		petService.save(pet);
	}
	
	@Test
	public void shouldSavePetInRepository() {
		PetServiceImpl petService = new PetServiceImpl();
		PetRepository mockRepo = mock(PetRepository.class);
		petService.setPetRepository(mockRepo);
		
		Pet pet = new Pet();
		pet.setId(1L);
		petService.save(pet);
		
		verify(mockRepo).save(pet);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotCreateExistentPet() {
		PetServiceImpl petService = new PetServiceImpl();
		Pet pet = new Pet();
		pet.setId(1L);
		petService.create(pet, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowNullOwner() {
		PetServiceImpl petService = new PetServiceImpl();
		Pet pet = new Pet();
		petService.create(pet, null);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void shouldNotAllowNullOrganization() {
		PetServiceImpl petService = new PetServiceImpl();
		Pet pet = new Pet();
		User owner = new User();
		petService.create(pet, owner);
	}
	
	@Test
	public void newPetShouldHaveOwnerAndOrganization() {
		Organization o = new Organization();
		o.setId(1L);
		
		User owner = new User();
		owner.setId(2L);
		owner.setOrganization(o);
		
		Pet p = new Pet();
		
		PetServiceImpl petService = new PetServiceImpl();
		PetRepository mockRepo = mock(PetRepository.class);
		petService.setPetRepository(mockRepo);
		
		when(mockRepo.save(p)).thenReturn(p);
		Pet got = petService.create(p, owner);
		
		verify(mockRepo).save(p);
		assertThat(got, equalTo(p));
		assertThat(got.getOwner(), equalTo(owner));
		assertThat(got.getOrganization(), equalTo(o));
		assertThat(got.getCreationDateTime(), notNullValue());
	}
	
}
