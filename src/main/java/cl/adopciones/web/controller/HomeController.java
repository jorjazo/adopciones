package cl.adopciones.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @RequestMapping({ "/", "/home" })
    public String home(Model model) {
        return "home";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/acerca")
    public String about() {
    	return "about";
    }
    
    @GetMapping("/rescatistas")
    public String organizations() {
    	return "organizations";
    }
    
}
