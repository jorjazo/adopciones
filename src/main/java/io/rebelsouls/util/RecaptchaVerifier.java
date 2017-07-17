package io.rebelsouls.util;

import javax.servlet.http.HttpServletRequest;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class RecaptchaVerifier {

    @NonNull
    private String recaptchaSecret;
    
    public boolean verify(HttpServletRequest request) {
        
        String token = request.getParameter("g-recaptcha-response");
        String remoteIp = request.getRemoteAddr();
        
        try {
            HttpResponse<JsonNode> jsonResponse = 
                    Unirest
                        .post("https://www.google.com/recaptcha/api/siteverify")
                        .field("secret", recaptchaSecret)
                        .field("response", token)
                        .field("remoteip", remoteIp)
                        .asJson();
            
            boolean result = jsonResponse.getBody().getObject().getBoolean("success");
            log.error(
                    new LogBuilder()
                        .with("event", "verifyRecaptcha")
                        .with("result", Boolean.toString(result))
                        .build());
            
            return result;
            
        } catch (UnirestException e) {
            log.error(
                    new LogBuilder()
                        .with("event", "verifyRecaptcha")
                        .with("result", "error")
                        .with("exception", e.toString())
                        .build(),
                    e);
        }
                
        
        return false;
    }
    
}
