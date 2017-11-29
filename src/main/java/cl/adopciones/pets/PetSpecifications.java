package cl.adopciones.pets;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

public class PetSpecifications {

	public static Specification<Pet> hasName(String name) {
		return new Specification<Pet>() {
			@Override
			public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(name == null)
					return cb.and();
				return cb.equal(root.get("name"), name);
			}
			
		};
	}
	
	public static Specification<Pet> genderIn(Gender[] genders) {
		return new Specification<Pet>() {
			@Override
			public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(genders == null)
					return cb.or();
				
				return root.get("gender").in((Object[]) genders);
			}
			
		};
	}
	
	public static Specification<Pet> typeIn(PetType[] types) {
		return new Specification<Pet>() {
			@Override
			public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(types == null)
					return cb.or();
				
				return root.get("type").in((Object[]) types);
			}
			
		};
	}
	
	public static Specification<Pet> sizeIn(PetSizeCategory[] sizes) {
		return new Specification<Pet>() {
			@Override
			public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(sizes == null)
					return cb.or();
				
				return root.get("sizeCategory").in((Object[]) sizes);
			}
			
		};
	}
	
	public static Specification<Pet> ageIn(PetAgeCategory[] ages) {
		return new Specification<Pet>() {
			@Override
			public Predicate toPredicate(Root<Pet> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				if(ages == null)
					return cb.or();
				
				return root.get("ageCategory").in((Object[]) ages);
			}
			
		};
	}
	
}
