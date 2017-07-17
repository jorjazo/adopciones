package io.rebelsouls.template.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.rebelsouls.core.users.Role;
import io.rebelsouls.core.users.User;
import io.rebelsouls.core.users.UserAlreadyValidatedException;
import io.rebelsouls.template.services.EmailService;
import io.rebelsouls.template.services.UserService;
import io.rebelsouls.template.web.exceptions.InvalidRequestException;
import io.rebelsouls.template.web.exceptions.ResourceNotFoundException;
import io.rebelsouls.template.web.forms.UserRegistrationForm;
import io.rebelsouls.util.RecaptchaVerifier;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RecaptchaVerifier recaptchaVerifier;
    
    @Value("${app.url}")
    private String appUrl;
    
    @Value("${app.recaptcha.key}")
    private String recaptchaKey;

    @GetMapping("")
    public String displayUserListAndForm(@AuthenticationPrincipal User currentUser, Model model) {
        model.addAttribute("recaptchaKey", recaptchaKey);
        
        if (currentUser == null)
            return "users/form";

        if (currentUser.hasRole("ADMIN")) {
            model.addAttribute("userPage", userService.getPageOfUsers(0, 3));
            return "users/list";
        }

        return "users/form";
    }

    @PostMapping("")
    public String registerUser(@AuthenticationPrincipal User currentUser, @Valid UserRegistrationForm form,
            BindingResult validation, Model model, HttpServletRequest request) {
        model.addAttribute("formData", form);
        model.addAttribute("validation", validation);
        model.addAttribute("recaptchaKey", recaptchaKey);
        
        if(!recaptchaVerifier.verify(request)) {
            model.addAttribute("recaptchaError", true);
            return "users/form";
        }
        
        if (!form.getPassword().equals(form.getPasswordRepeat())) {
            validation.rejectValue("passwordRepeat", "Debe coincidir con la contraseña ingresada");
        }

        if (validation.hasErrors()) {
            return "users/form";
        }

        User newUser = userService.createUser(form.getEmail(), form.getPassword(), form.getName(),
                Role.valueOf(form.getUserType()));

        Map<String, Object> values = new HashMap<>();
        values.put("newUser", newUser);
        values.put("validationUrl",
                appUrl + "/users/" + newUser.getId() + "/validate?token=" + newUser.getValidationToken());

        emailService.emailUser(newUser, "¡Bienvenido a Template App!",
                "classpath:/templates/users/registration-email", values);

        return "users/create-success";
    }

    @GetMapping("/{userId}/validate")
    public String validateUser(@PathVariable("userId") Long userId,
            @RequestParam(name = "token", required = true) String validationToken) {
        try {
            userService.validateUser(userId, validationToken);
        } catch (UserAlreadyValidatedException e) {
            return "redirect:/login";
        }

        // TODO: configure security to make it automatically redirect to login
        // page and come back to home
        return "redirect:/users/" + userId;
    }

    @GetMapping("/{userId}")
    @PreAuthorize("isAuthenticated() and (hasRole('ADMIN') or principal.id == #userId)")
    public String userDetails(@PathVariable("userId") Long userId, @AuthenticationPrincipal User currentUser ,Model model) {
        User user = userService.loadUserById(userId);
        
        if(user == null)
            throw new ResourceNotFoundException();

        model.addAttribute("user", user);
        if(user.getId().equals(currentUser.getId()))
            return "users/home";
        else if(currentUser.hasRole("ADMIN"))
            return "users/details";
        
        //Should not get here
        throw new InvalidRequestException();
    }
    
}
