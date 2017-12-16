package cl.adopciones.pets;

import org.springframework.data.jpa.domain.Specification;

import io.rebelsouls.chile.Comuna;
import io.rebelsouls.chile.Provincia;
import io.rebelsouls.chile.Region;

public class PetSpecifications {

	private static final String PET_NAME_PROPERTY = "name";
	private static final String PET_GENDER_PROPERTY = "gender";
	private static final String PET_TYPE_PROPERTY = "type";
	private static final String PET_SIZE_CATEGORY_PROPERTY = "sizeCategory";
	private static final String PET_AGE_CATEGORY_PROPERTY = "ageCategory";
	private static final String PET_LOCATION_PROPERTY = "location";

	private PetSpecifications() {
		
	}
	
	public static Specification<Pet> hasName(String name) {
		return (root, query, cb) -> {
			if(name == null)
				return cb.and();
			return cb.equal(root.get(PET_NAME_PROPERTY), name);
		};
	}
	
	public static Specification<Pet> genderIn(Gender[] genders) {
		return (root, query, cb) -> {
			if(genders == null)
				return cb.or();
			
			return root.get(PET_GENDER_PROPERTY).in((Object[]) genders);
		};
	}
	
	public static Specification<Pet> typeIn(PetType[] types) {
		return (root, query, cb) -> {
			if(types == null)
				return cb.or();
			
			return root.get(PET_TYPE_PROPERTY).in((Object[]) types);
		};
	}
	
	public static Specification<Pet> sizeIn(PetSizeCategory[] sizes) {
		return (root, query, cb) -> {
			if(sizes == null)
				return cb.or();
			
			return root.get(PET_SIZE_CATEGORY_PROPERTY).in((Object[]) sizes);
		};
	}
	
	public static Specification<Pet> ageIn(PetAgeCategory[] ages) {
		return (root, query, cb) -> {
			if(ages == null)
				return cb.or();
			
			return root.get(PET_AGE_CATEGORY_PROPERTY).in((Object[]) ages);
		};
	}
	
	public static Specification<Pet> inLocation(Region region, Provincia provincia, Comuna comuna) {
		return (root, query, cb) -> {
			if(comuna != null)
				return cb.equal(root.get(PET_LOCATION_PROPERTY), comuna);
			
			if(provincia != null)
				return root.get(PET_LOCATION_PROPERTY).in(provincia.getComunas().toArray());
			
			if(region != null)
				return root.get(PET_LOCATION_PROPERTY).in(region.getComunas().toArray());
			
			return cb.and();
		};
	}
	
}
