package cl.adopciones.web.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import cl.adopciones.pets.Gender;
import cl.adopciones.pets.PetAgeCategory;
import cl.adopciones.pets.PetSizeCategory;
import cl.adopciones.pets.PetType;
import cl.adopciones.web.forms.PetForm;
import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;
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

	@Test
	public void shouldDisplayExistingPet() throws Exception {
		mockMvc.perform(get("/mascotas/1"))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
			.andExpect(content().encoding("UTF-8"))
			.andExpect(content().string(containsString("Panzer")))
			;
	}
	
	@Test
	@DirtiesContext
	public void shouldCreateANewPet() throws Exception {
		PetForm form = new PetForm();
		form.setAgeCategory(PetAgeCategory.OLDER_THAN_SIX_YEARS);
		form.setComuna(Comuna.Algarrobo);
		form.setDescription("Un lindo gatito");
		form.setGender(Gender.FEMALE);
		form.setName("Misifús");
		form.setProvincia(Provincia.San_Antonio);
		form.setRegion(Region.Valparaiso);
		form.setSizeCategory(PetSizeCategory.L);
		form.setType(PetType.CAT);
		
		mockMvc.perform(post("/mascotas", form).with(csrf()))
			.andExpect(status().isOk())
		;
		
	}
}
