package cl.adopciones.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;

@RestController
@RequestMapping("/geo")
public class GeoInfoController {

	@GetMapping("")
	public ResponseEntity<Map<String, String>> getRegiones() {
		Map<String, String> values = new HashMap<>();
		Stream.of(Region.values()).forEach(r -> values.put(r.name(), r.getNombre()));
		return new ResponseEntity<>(values, HttpStatus.OK) ;
	}
	
	@GetMapping("/{region}")
	public ResponseEntity<Map<String, String>> getProvincias(@PathVariable("region") Region region) {
		if(region == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		Map<String, String> values = new HashMap<>();
		region.getProvincias().forEach(p -> values.put(p.name(), p.getNombre()));
		return new ResponseEntity<>(values, HttpStatus.OK) ;
	}
	
	@GetMapping("/{region}/{provincia}") 
	public ResponseEntity<Map<String, String>> getComunas(@PathVariable("region") Region region, @PathVariable("provincia") Provincia provincia) {
		if(provincia == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		
		Map<String, String> values = new HashMap<>();
		provincia.getComunas().forEach(c -> values.put(c.name(), c.getNombre()));
		return new ResponseEntity<>(values, HttpStatus.OK) ;
	}
	
}
