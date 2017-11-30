package cl.adopciones.web.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import io.rebelsouls.chile.Region;

@ControllerAdvice
public class GeoInfoAdvice {

	@ModelAttribute("regiones")
	public Region[] getAllRegions() {
		return Region.values();
	}
	
}
