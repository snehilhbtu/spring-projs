package com.springboot.blog.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticatcationEntryPoint implements AuthenticationEntryPoint {
    @Override
    //it will only send error
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}
/*
===========================================================
🔐 JwtAuthenticatcationEntryPoint - EXPLAINED
===========================================================

✅ ROLE:
  - Acts as the entry point for handling unauthorized access.
  - Triggered when a user tries to access a secured endpoint
    WITHOUT proper authentication (like missing or invalid JWT).

✅ WHY IT’S NEEDED:
  - In REST APIs, we don’t want to redirect to a login page.
  - Instead, we return a 401 Unauthorized response directly.

✅ WHEN IT’S USED:
  - Configured in SecurityConfig.java:
        .exceptionHandling(exception ->
            exception.authenticationEntryPoint(authenticatcationEntryPoint))

  - If authentication fails or is missing,
    this class’s commence() method is automatically invoked.

✅ WHAT IT DOES:
  - Simply sends a 401 Unauthorized HTTP status with an error message.
===========================================================
*/