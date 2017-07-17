package io.rebelsouls.template.web.advice;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.rebelsouls.core.users.User;

@ControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler(Throwable.class)
    public String handleError(Throwable error, Model model, HttpServletResponse response) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null && user instanceof User)
            model.addAttribute("currentUser", user);
        
        ResponseStatus annotatedStatus = error.getClass().getAnnotation(ResponseStatus.class);
        if(annotatedStatus != null) {
            HttpStatus status = annotatedStatus.code();
            response.setStatus(status.value());
        }
        else
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        model.addAttribute("error", error);
        model.addAttribute("stackTrace", buildStackTraceForThrowable(error));
        return "base/generic-error";
    }
    
    private String buildStackTraceForThrowable(Throwable t) {
        StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return t.toString();
    }
}

