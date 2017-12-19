package cl.adopciones.web.controller;

import static io.rebelsouls.test.CompositeResultMatcher.badRequestWithRedirect;
import static io.rebelsouls.test.CompositeResultMatcher.createdAndRedirectResponse;
import static io.rebelsouls.test.CompositeResultMatcher.htmlOkResponse;
import static io.rebelsouls.test.CompositeResultMatcher.redirectResponseWithUrl;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
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
import io.rebelsouls.photos.PhotoSize;
import io.rebelsouls.services.StorageService;
import io.rebelsouls.storage.StorageResource;
import io.rebelsouls.storage.StorageResourceDescription;

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

	@Value("classpath:/static/img/logo-edra.jpg")
	private Resource testPhoto;

	@Test
	public void shouldDisplayExistingPet() throws Exception {
		mockMvc.perform(get(PetsController.URL_PREFIX + "/1"))
			.andExpect(htmlOkResponse())
			.andExpect(content().string(containsString("Olivia")))
			.andExpect(content().string(containsString("2017")))
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
	
	@Test
	public void shouldDisplayPetPhoto() throws Exception {
		String photoPath = "1/photos/0/original";
		StorageResourceDescription description = createTestResourceDescription(photoPath);
		StorageResource resource = createTestResource(photoPath);
		
		when(storageService.list(anyString()))
			.thenReturn(Arrays.asList(description));
		
		when(storageService.load(startsWith("1/photos/0"))).thenReturn(resource);
		
		mockMvc.perform(get(PetsController.URL_PREFIX + "/1/fotos/0/" + PhotoSize.ORIGINAL.getName()))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.IMAGE_JPEG))
			;
	}

	private StorageResourceDescription createTestResourceDescription(String photoPath) throws IOException {
		StorageResourceDescription description = new StorageResourceDescription();
		description.setContentLength(testPhoto.contentLength());
		description.setLastModified(new Date(testPhoto.lastModified()));
		description.setPath(photoPath);
		return description;
	}

	private StorageResource createTestResource(String photoPath) throws IOException {
		StorageResource resource = new StorageResource();
		resource.setContentLength(testPhoto.contentLength());
		resource.setPath(photoPath);
		resource.setLastModified(new Date(testPhoto.lastModified()));
		resource.setContentStream(testPhoto.getInputStream());
		resource.setContentType(MediaType.IMAGE_JPEG_VALUE);
		return resource;
	}
	
	@Test
	public void shouldAcceptPetPhoto() throws Exception {
		User user = userService.loadUserById(2L);
		MockMultipartFile testFile = new MockMultipartFile("file", "file.jpg", "image/jpeg", testPhoto.getInputStream());
		mockMvc.perform(fileUpload(PetsController.URL_PREFIX + "/1/fotos")
				.file(testFile)
				.with(user(user))
				.with(csrf())
				)
			.andExpect(redirectResponseWithUrl(PetsController.URL_PREFIX + "/1"));
		verify(storageService, times(3)).store(anyString(), any());
	}
	
//	@Test
//	public void shouldRegisterPetAdoption() throws Exception {
//		mockMvc.perform(post(PetsController.URL_PREFIX + "/1/adopciones")
//				.with(csrf())
//				.with(newTestAdoptionParams())
//				)
//			.andExpect(createdAndRedirectResponse("/mascotas/1"));
//		;
//		
//		// Otro rq a la mascota para verificar algun mensaje
//	}
//	
	@Test
	public void shouldNotAcceptInvalidAdoptionForm() throws Exception {
		mockMvc.perform(post(PetsController.URL_PREFIX + "/1/adopciones")
				.with(csrf())
				.with(newTestAdoptionParams())
				.with(invalidEmail())
				)
			.andExpect(badRequestWithRedirect("/mascotas/1"));
		;
		
	}
	
	
	private RequestPostProcessor newTestAdoptionParams() {
		return new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.addParameter("isLegalAge", "1");
				request.addParameter("acceptsResposability", "1");
				request.addParameter("understandsCosts", "1");
				request.addParameter("name", "Jorge Valencia");
				request.addParameter("rut", "1.234.567-8");
				request.addParameter("email", "jorge.valencia@bodoque.cl");
				request.addParameter("phoneNumber", "987654321");
				return request;
			}
		};
	}
	
	private RequestPostProcessor invalidEmail() {
		return new RequestPostProcessor() {
			@Override
			public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
				request.removeParameter("email");
				request.addParameter("email", "not valid");
				return request;
			}
		};
	}
}
