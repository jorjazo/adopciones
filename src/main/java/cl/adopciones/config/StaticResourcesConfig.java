package cl.adopciones.config;

import java.util.concurrent.TimeUnit;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class StaticResourcesConfig extends ResourceHandlersConfigurer implements WebMvcConfigurer {

	@Override 
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/static/**")
		.addResourceLocations("classpath:/static/")
		.setCacheControl(CacheControl.maxAge(1, TimeUnit.HOURS).cachePublic());
	}

}
