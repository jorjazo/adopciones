package cl.adopciones.web.forms;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import io.rebelsouls.util.Patterns;
import lombok.Data;

@Data
public class UserRegistrationForm {
	@Size(min=5,max=100)
	@NotNull
	private String name;
	
	@Size(min=5,max=100)
	@Pattern(regexp=Patterns.EMAIL_REGEX)
	@NotNull
	private String email;
	
	@Size(min=6,max=100)
	@NotNull
	private String password;
	
	@Size(min=6,max=100)
	@NotNull
	private String passwordRepeat;
	
}
