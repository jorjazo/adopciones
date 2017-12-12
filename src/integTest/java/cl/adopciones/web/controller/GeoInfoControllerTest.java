package cl.adopciones.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
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

import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
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
			.andExpect(jsonPath("$." + Region.La_Araucanía.name(), is(Region.La_Araucanía.getNombre())))
		;
	}
	
	@Test
	public void shouldNotFindInexistentRegion() throws Exception {
		mockMvc.perform(get("/geo/bla"))
			.andExpect(status().isNotFound())
		;
	}
	
	@Test
	public void shouldReturnListOfProvincia() throws Exception {
		mockMvc.perform(get("/geo/{region}", Region.Libertador_General_Bernardo_Ohiggins.name()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.*", hasSize(Region.Libertador_General_Bernardo_Ohiggins.getProvincias().size())))
			.andExpect(jsonPath("$." + Provincia.Cardenal_Caro.name(), is(Provincia.Cardenal_Caro.getNombre())))
		;
	}
	
	@Test
	public void shouldNotFindInexistentProvincia() throws Exception {
		mockMvc.perform(get("/geo/{region}/provincia", Region.Antofagasta.name()))
			.andExpect(status().isNotFound())
		;
	}
	
	@Test
	public void shouldNotFindNonMemberProvicia() throws Exception {
		mockMvc.perform(get("/geo/{region}/{provincia}", Region.Antofagasta.name(), Provincia.Aisén.name()))
			.andExpect(status().isNotFound())
		;
	}
	
	@Test
	public void shouldReturnListOfComuna() throws Exception {
		mockMvc.perform(get("/geo/{region}/{provincia}", Region.Atacama, Provincia.Copiapó))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.*", hasSize(Provincia.Copiapó.getComunas().size())))
			.andExpect(jsonPath("$." + Comuna.Copiapó, is(Comuna.Copiapó.getNombre())))
		;
	}
	
}
