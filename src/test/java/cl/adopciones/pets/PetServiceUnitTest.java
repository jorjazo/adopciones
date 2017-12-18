package cl.adopciones.pets;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.LinkedList;

import org.junit.Test;

import cl.adopciones.organizations.Organization;
import cl.adopciones.users.User;
import io.rebelsouls.photos.PhotoSize;
import io.rebelsouls.services.StorageService;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;

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
	
	
	@Test
	public void shouldGetPetFromRepository() {
		PetServiceImpl petService = new PetServiceImpl();
		PetRepository mockRepo = mock(PetRepository.class);
		petService.setPetRepository(mockRepo);
		
		Organization o = new Organization();
		o.setId(1L);
		o.setContactEmail("bla");
		
		User owner = new User();
		owner.setId(2L);
		owner.setOrganization(o);
		
		Pet p = new Pet();
		p.setId(1L);
		p.setOrganization(o);
		p.setOwner(owner);
		
		when(mockRepo.findOne(1L)).thenReturn(p);
		
		Pet got = petService.getPet(1L);
		assertThat(got, equalTo(p));
	}
	
	@Test(expected=PetPhotoLimitException.class)
	public void shouldNotAllowMorePhotosThanLimit() throws Exception {
		StorageService storageMock = mock(StorageService.class);
		PetServiceImpl petService = new PetServiceImpl();
		petService.setStorageService(storageMock);
		
		petService.setMaxPhotosPerPet(0);
		when(storageMock.list(anyString())).thenReturn(new LinkedList<>());
		
		petService.addPetPhoto(new Pet(), new File("bla.txt"));
	}
	
	@Test
	public void shouldGetPhotoInfoFromCache() {
		StorageService storageMock = mock(StorageService.class);
		PetServiceImpl petService = new PetServiceImpl();
		petService.setStorageService(storageMock);
		
		StorageResource resource = new StorageResource();
		when(storageMock.load(anyString()))
			.thenReturn(resource)
			.thenReturn(null);
		
		Pet testPet = new Pet();
		testPet.setId(1L);
		
		StorageResource photo = petService.getPetPhoto(testPet, 0, PhotoSize.ORIGINAL);
		assertThat(photo, equalTo(resource));
		
		StorageResourceDescription cache = petService.getPhotoCache(testPet, 0, PhotoSize.ORIGINAL);
		assertThat(cache, notNullValue());
		assertThat(cache, equalTo(photo));
		
		photo = petService.getPetPhoto(testPet, 0, PhotoSize.ORIGINAL);
		assertThat(photo, nullValue());
		
		cache = petService.getPhotoCache(testPet, 0, PhotoSize.ORIGINAL);
		assertThat(cache, nullValue());
	}
	
	
}
