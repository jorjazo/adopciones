package cl.adopciones.pets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import cl.adopciones.users.UserService;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.services.StorageService;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
@SpringBootTest
public class PetServiceTest {

	@Autowired
	PetService petService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	StorageService storageService;
	
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
		
		pet = petService.getPet(pet.getId());
		assertThat(pet).isEqualTo(found);
	}

	@Value("classpath:/static/img/logo-edra.jpg") Resource testPhoto;
	
	@Test
	public void shouldUploadPhotoAndTwoThumbs() throws IOException {
		
		when(storageService.list(anyString())).thenReturn(new LinkedList<>());

		Pet pet = petService.getPet(1L);
		try {
			petService.addPetPhoto(pet, testPhoto.getFile());
		} catch (PetPhotoException e) {
			fail();
		}
		
		verify(storageService, times(3)).store(anyString(), any());
	}
	
	@Test
	public void shouldGetThreeTypesOfPhotos() {
		
		when(storageService.list(anyString())).thenReturn(
				Stream.of("1/photos/0", "1/photos/1")
					.map(s -> new StorageResourceDescription(0, null, s))
					.collect(Collectors.toList())
			);
		
		when(storageService.load(anyString()))
			.thenReturn(null);
		
		Pet pet = petService.getPet(1L);
		petService.getPetPhoto(pet, 0, PhotoSize.original);
		petService.getPetPhoto(pet, 0, PhotoSize.fixed_height);
		petService.getPetPhoto(pet, 0, PhotoSize.fixed_width);
		
		ArgumentCaptor<String> arg = ArgumentCaptor.forClass(String.class);
		
		verify(storageService, times(3)).load(arg.capture());
		
		Set<String> allValues = new HashSet<>();
		allValues.addAll(arg.getAllValues());
		assertThat(allValues).hasSameElementsAs(arg.getAllValues()).hasSameSizeAs(arg.getAllValues());
	}
	
	@Test
	public void shouldCacheDescriptions() {
		StorageResource photo = new StorageResource();
		when(storageService.load(anyString()))
			.thenReturn(null)
			.thenReturn(photo);
		
		Pet pet = petService.getPet(1L);
		petService.getPetPhoto(pet, 0, PhotoSize.original);
		StorageResource got = petService.getPetPhoto(pet, 0, PhotoSize.original);
		StorageResourceDescription gotSecond = petService.getPhotoCache(pet, 0, PhotoSize.original);
		assertThat(got).isSameAs(photo).isSameAs(gotSecond);
		verify(storageService, times(2)).load(anyString());
	}
	
	@Test
	public void shouldRemoveNullResponses() {
		StorageResource photo = new StorageResource();
		when(storageService.load(anyString()))
			.thenReturn(photo)
			.thenReturn(null);
		
		Pet pet = petService.getPet(1L);
		petService.getPetPhoto(pet, 0, PhotoSize.original);
		petService.getPetPhoto(pet, 0, PhotoSize.original);
		StorageResourceDescription gotCache = petService.getPhotoCache(pet, 0, PhotoSize.original);
		
		assertThat(gotCache).isNull();
	}
	
	@Test
	public void shouldLimitNumberOfPhotosPerPet() throws IOException {
		
		//debería mejorar la configuración del número máximo de fotos
		when(storageService.list(anyString())).thenReturn(
				Stream.of(0, 1, 2, 3, 4, 5)
					.map(i -> "1/photos/" + i)
					.map(s -> new StorageResourceDescription(0, null, s))
					.collect(Collectors.toList())
			);
		
		Pet pet = petService.getPet(1L);
		
		Exception e = null;
		try {
			petService.addPetPhoto(pet, testPhoto.getFile());
		} catch (PetPhotoException ppe) {
			e = ppe;
		}
		
		assertThat(e).isNotNull();
		verify(storageService, never()).store(anyString(), any());
	}
}
