package cl.adopciones.web.advice;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import cl.adopciones.users.User;

@ControllerAdvice
public class ErrorHandlerAdvice {

    @ExceptionHandler(Exception.class)
    public String handleError(Exception error, Model model, HttpServletResponse response) {
        Object user = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(user != null && user instanceof User)
            model.addAttribute("currentUser", user);
        
        ResponseStatus annotatedStatus = error.getClass().getAnnotation(ResponseStatus.class);
        if(annotatedStatus != null) {
            HttpStatus status = annotatedStatus.code();
            response.setStatus(status.value());
        }
        else if (error instanceof AccessDeniedException)
        	response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        else
            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        
        model.addAttribute("error", error);
        String stackTrace = buildStackTraceForThrowable(error);
        model.addAttribute("stackTrace", stackTrace);
        
        return "base/generic-error";
    }
    
    private String buildStackTraceForThrowable(Throwable t) {
        StringWriter stackTrace = new StringWriter();
        t.printStackTrace(new PrintWriter(stackTrace));
        return t.toString();
    }
}

