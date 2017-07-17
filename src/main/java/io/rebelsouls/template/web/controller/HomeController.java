package io.rebelsouls.template.web.controller;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.rebelsouls.core.users.User;

@Controller
public class HomeController {

	@RequestMapping({ "/", "/home" })
	public String home(Model model, @AuthenticationPrincipal User user) {
		return "home";
	}

	@GetMapping("/login")
	public String loginForm() {
		return "login";
	}

	@ExceptionHandler
	public String handleException(Exception e, Model model) {
		model.addAttribute("message", e.toString());

		StringWriter stackTraceWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(stackTraceWriter));
		model.addAttribute("stackTrace", stackTraceWriter.toString());
		return "base/generic-error";
	}
}
