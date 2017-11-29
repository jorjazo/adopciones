package cl.adopciones.pets;

import lombok.Getter;

public enum PetSizeCategory {
	XS(1),
	S(2),
	M(3),
	L(4),
	XL(5);

	@Getter
	private Integer size = null;
	
	private PetSizeCategory(int size) {
		this.size = size;
	}
}
