package cl.adopciones.web.controller;

import static io.rebelsouls.test.CompositeResultMatcher.createdAndRedirectResponse;
import static io.rebelsouls.test.CompositeResultMatcher.htmlOkResponse;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

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
			.andExpect(htmlOkResponse())
			.andExpect(content().string(containsString("Panzer")))
			;
	}
	
	@Test
	@DirtiesContext
	public void shouldCreateANewPet() throws Exception {
		String petName = "Misifús";
		User user = userService.loadUserById(2L);
		
		mockMvc.perform(post(PetsController.URL_PREFIX).with(csrf())
				.with(user(user))
				.with(newTestPetParams())
				.param("name", petName)
				)
			.andExpect(createdAndRedirectResponse("/mascotas/??"));
		;
	}
	
	private RequestPostProcessor newTestPetParams() {
		return new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.addParameter("ageCategory", PetAgeCategory.OLDER_THAN_SIX_YEARS.name());
				request.addParameter("comuna", Comuna.Algarrobo.name());
				request.addParameter("description", "Un lindo gatito");
				request.addParameter("gender", Gender.FEMALE.name());
				request.addParameter("sizeCategory", PetSizeCategory.L.name());
				request.addParameter("type", PetType.CAT.name());
				return request;
			}
		};
	}
	
	@Test
	public void shouldNotCreateAPetIfMissingData() throws Exception {
		User user = userService.loadUserById(2L);
		mockMvc.perform(post(PetsController.URL_PREFIX)
				.with(csrf())
				.with(user(user))
				.with(newTestPetParams())
				)
			.andExpect(status().isBadRequest()) //missing name
		;
	}
	
	@Test
	public void shouldNotCreateAPetIfNotAUser() throws Exception {
		mockMvc.perform(post(PetsController.URL_PREFIX)
				.with(csrf())
				.with(newTestPetParams())
				.param("name", "Paparaipicoipi")
				)
			.andExpect(status().isUnauthorized())
		;
	}
	
	@Test
	public void shouldDisplayAFormToCreateANewPet() throws Exception {
		mockMvc.perform(get(PetsController.URL_PREFIX + "/nuevo"))
		.andExpect(htmlOkResponse())
		.andExpect(content().string(containsString("MALE")))
		;
	}
}
