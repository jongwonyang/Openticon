package io.ssafy.openticon.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;

            // Get the current authentication object
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = "Unsigned";

            if (authentication != null && authentication.isAuthenticated()) {
                Object principal = authentication.getPrincipal();
                if (principal instanceof UserDetails) {
                    username = ((UserDetails) principal).getUsername(); // Get the username
                } else {
                    username = principal.toString(); // Handle cases like JWT-based authentication
                }
            }

            // Log the request details
            System.out.println(String.format("[HTTP Method: %s] [Request URI: %s] [Username: %s]", request.getMethod(), request.getRequestURI(), username));
        }
        return true; // true to continue request processing
    }
}