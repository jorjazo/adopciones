package cl.adopciones.pets;

import org.springframework.data.jpa.domain.Specification;

import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;

public class PetSpecifications {

	private PetSpecifications() {
		
	}
	
	public static Specification<Pet> hasName(String name) {
		return (root, query, cb) -> {
			if(name == null)
				return cb.and();
			return cb.equal(root.get("name"), name);
		};
	}
	
	public static Specification<Pet> genderIn(Gender[] genders) {
		return (root, query, cb) -> {
			if(genders == null)
				return cb.or();
			
			return root.get("gender").in((Object[]) genders);
		};
	}
	
	public static Specification<Pet> typeIn(PetType[] types) {
		return (root, query, cb) -> {
			if(types == null)
				return cb.or();
			
			return root.get("type").in((Object[]) types);
		};
	}
	
	public static Specification<Pet> sizeIn(PetSizeCategory[] sizes) {
		return (root, query, cb) -> {
			if(sizes == null)
				return cb.or();
			
			return root.get("sizeCategory").in((Object[]) sizes);
		};
	}
	
	public static Specification<Pet> ageIn(PetAgeCategory[] ages) {
		return (root, query, cb) -> {
			if(ages == null)
				return cb.or();
			
			return root.get("ageCategory").in((Object[]) ages);
		};
	}
	
	public static Specification<Pet> inLocation(Region region, Provincia provincia, Comuna comuna) {
		return (root, query, cb) -> {
			if(comuna != null)
				return cb.equal(root.get("location"), comuna);
			
			if(provincia != null)
				return root.get("location").in(provincia.getComunas().toArray());
			
			if(region != null)
				return root.get("location").in(region.getComunas().toArray());
			
			return cb.and();
		};
	}
	
}
