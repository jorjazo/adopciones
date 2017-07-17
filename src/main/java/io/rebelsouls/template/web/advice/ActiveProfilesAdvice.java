package io.rebelsouls.template.web.advice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ActiveProfilesAdvice {

	@Value("${spring.profiles.active}")
	private String activeProfiles; 
	
	@ModelAttribute("active_profiles")
	public String getActiveProfiles() {
		return activeProfiles;
	}
	
}
