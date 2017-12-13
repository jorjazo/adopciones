package cl.adopciones.web.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import cl.adopciones.pets.Gender;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetSizeCategory;
import cl.adopciones.pets.PetType;
import cl.adopciones.users.User;
import cl.adopciones.users.UserService;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.services.StorageService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class PetControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private StorageService storageService;

	@Autowired
	private UserService userService;
	
	@Test
	public void shouldDisplayExistingPet() throws Exception {
		mockMvc.perform(get(PetsController.URL_PREFIX + "/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(content().encoding("UTF-8"))
			.andExpect(content().string(containsString("Panzer")))
			;
	}
	
	@Test
	@DirtiesContext
	public void shouldCreateANewPet() throws Exception {
		String petName = "Misifús";
		User user = userService.loadUserById(2L);
		
		mockMvc.perform(post(PetsController.URL_PREFIX).with(csrf()).with(user(user))
				.param("name", petName)
				.param("ageCategory", PetAgeCategory.OLDER_THAN_SIX_YEARS.name())
				.param("comuna", Comuna.Algarrobo.name())
				.param("description", "Un lindo gatito")
				.param("gender", Gender.FEMALE.name())
				.param("sizeCategory", PetSizeCategory.L.name())
				.param("type", PetType.CAT.name())
				)
			.andExpect(status().isCreated())
			.andExpect(redirectedUrlPattern("/mascotas/??"))
		;
	}
	
	@Test
	public void shouldNotCreateAPetIfMissingData() throws Exception {
		String petName = "Misifús";
		User user = userService.loadUserById(2L);
		
		mockMvc.perform(post(PetsController.URL_PREFIX).with(csrf()).with(user(user))
				.param("name", petName)
//				.param("ageCategory", PetAgeCategory.OLDER_THAN_SIX_YEARS.name())
				.param("comuna", Comuna.Algarrobo.name())
				.param("description", "Un lindo gatito")
				.param("gender", Gender.FEMALE.name())
				.param("sizeCategory", PetSizeCategory.L.name())
				.param("type", PetType.CAT.name())
				)
			.andExpect(status().isBadRequest())
		;
	}
	
	@Test
	public void shouldDisplayAFormToCreateANewPet() throws Exception {
		mockMvc.perform(get(PetsController.URL_PREFIX + "/nuevo"))
		.andExpect(status().isOk())
		.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
		.andExpect(content().encoding("UTF-8"))
		.andExpect(content().string(containsString("MALE")))
		;
	}
}
