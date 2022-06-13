package com.deta.kopmart_backend.security.JWT;

import com.deta.kopmart_backend.entity.User;
import com.deta.kopmart_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author deta
 * @description Class for filtering the Json Web Token Request
 */
@Component
public class JwtFilter extends OncePerRequestFilter{
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserService userService;

    /**
     * @param httpServletRequest The request object
     * @param httpServletResponse The response object
     * @param filterChain The filter chain
     * @throws ServletException If there is an error processing the request
     * @throws IOException If there is an error writing the response
     * @description This method is called when the request is received. It checks if the request is a Json Web Token request.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getToken(httpServletRequest);
        if (jwt != null && jwtProvider.validate(jwt)) {
            try {
                String userAccount = jwtProvider.getUserAccount(jwt);
                User user = userService.findOne(userAccount);
                // pwd not necessary
                // if jwt ok, then authenticate
                SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole());
                ArrayList<SimpleGrantedAuthority> list = new ArrayList<>();
                list.add(sga);
                UsernamePasswordAuthenticationToken auth
                        = new UsernamePasswordAuthenticationToken(user.getEmail(), null, list);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (Exception e) {
                logger.error("Set Authentication from JWT failed");
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     * @param request The request object
     * @return Authorization header value
     * @description This method is used to get the Json Web Token from the request.
     */
    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.replace("Bearer ", "");
        }

        return null;
    }
}
