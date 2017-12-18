package cl.adopciones.pets;

import java.io.File;

import io.rebelsouls.photos.PhotoSize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@ToString
public class ThumbGenerationException extends RuntimeException {
	
	private String originalFilePath;
	private PhotoSize size;
	private File toFile;
	private Exception cause;
	
	private static final long serialVersionUID = 2723596526342944431L;
	
}
