package com.deta.kopmart_backend.security.JWT;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author deta
 * @description JwtAuthenticationEntryPoint
 */
@Component
public class JwtEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(JwtEntryPoint.class);


    /**
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param authException AuthenticationException
     * @throws IOException IOException
     * @throws ServletException ServletException
     * @description method to handle json web token authentication entry point
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        logger.error("Unauthorized error. Message - {}", authException.getMessage());
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }
}
