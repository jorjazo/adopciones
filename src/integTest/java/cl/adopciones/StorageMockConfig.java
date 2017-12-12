package cl.adopciones;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import io.rebelsouls.services.StorageService;

@Configuration
@Profile("test")
public class StorageMockConfig {

	@Bean
	@Primary
	public StorageService getStorageService() {
		return Mockito.mock(StorageService.class);
	}
}
