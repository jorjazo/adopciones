package cl.adopciones.pets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@ToString
public class PetPhotoLimitException extends Exception {
	private static final long serialVersionUID = -8054996577640900824L;

	private int photoNumber;
	private int maxPhotos;
	
}
