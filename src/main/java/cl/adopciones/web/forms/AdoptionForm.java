package cl.adopciones.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.rebelsouls.util.Patterns;
import lombok.Data;

@Data
public class AdoptionForm {

	@NotNull
	private Boolean isLegalAge;
	
	@NotNull
	private Boolean acceptsResponsability;
	
	@NotNull
	private Boolean understandsCosts;
	
	@NotNull
	@Size(min=5, max=100)
	private String name;
	
	@NotNull
	@Pattern(regexp=Patterns.EMAIL_REGEX)
	private String email;
	
	@NotNull
	@Size(min=8, max=20)
	@Pattern(regexp="^\\\\s*(\\\\d{7,9}-([kK0-9]))|(\\\\d{1,3}\\.\\\\d{3}\\.\\\\d{3}-[kK0-9])\\\\s*$")
	private String rut;
	
	@NotNull
	@Pattern(regexp="^\\\\s*(\\+\\d{11,13})|\\d{9,11}\\\\s*$")
	private String phoneNumber;
}
