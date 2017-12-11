package cl.adopciones.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import io.rebelsouls.chile.Region;
import io.rebelsouls.services.StorageService;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
public class GeoInfoControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	public GeoInfoController geoInfoController;
	
	@Mock
	private StorageService storageService;
	
	@Test
	public void contextLoads() {
		assertThat(geoInfoController).isNotNull();
	}
	
	
	@Test
	public void shouldReturnListOfRegions() throws Exception {
		mockMvc.perform(get("/geo/"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.*", hasSize(Region.values().length)))
		;
		
	}
}
