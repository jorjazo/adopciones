package cl.adopciones.pets;

public class PetPhotoLimitException extends PetPhotoException {
	private static final long serialVersionUID = -8054996577640900824L;

	public PetPhotoLimitException(Exception e) {
		super(e);
	}
	
	public PetPhotoLimitException() {
		super();
	}
}
