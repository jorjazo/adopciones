package io.rebelsouls.template.web.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class CsrfHtmlFieldAdvice {

	@ModelAttribute("csrfField")
	public String getCsrfHtmlField(HttpServletRequest request) {
		CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
		return "<input type=\"hidden\" name=\"" + token.getParameterName() + "\" value=\"" + token.getToken() + "\"/>";
	}
	
}
