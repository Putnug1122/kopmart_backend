package com.deta.kopmart_backend.controller;

import com.deta.kopmart_backend.entity.User;
import com.deta.kopmart_backend.security.JWT.JwtProvider;
import com.deta.kopmart_backend.service.UserService;
import com.deta.kopmart_backend.vo.request.LoginForm;
import com.deta.kopmart_backend.vo.response.JwtResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    AuthenticationManager authenticationManager;

    /**
     * @param loginForm - username (email) and password
     * @return - ResponseEntity<JwtResponse>
     * @description method for login with username (email) and password and return JSON Web Token
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginForm loginForm) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginForm.getUsername(), loginForm.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtProvider.generate(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User user = userService.findOne(userDetails.getUsername());
            return ResponseEntity.ok(new JwtResponse(jwt, user.getEmail(), user.getName(),user.getRole()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * @param user - User object
     * @return - ResponseEntity<User>
     * @description method for register new user
     */
    @PostMapping("/register")
    public ResponseEntity<User> save(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.save(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @param user - User object
     * @param principal - Principal object
     * @return - ResponseEntity<User>
     * @description method for update user profile
     */
    @PutMapping("/profile")
    public ResponseEntity<User> update(@RequestBody User user, Principal principal) {

        try {
            if (!principal.getName().equals(user.getEmail())) throw new IllegalArgumentException();
            return ResponseEntity.ok(userService.update(user));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * @param email - email of user
     * @param principal - Principal object
     * @return - ResponseEntity<User> - user object by email
     * @description method for get user object by email
     */
    @GetMapping("/profile/{email}")
    public ResponseEntity<User> getProfile(@PathVariable("email") String email, Principal principal) {
        if (principal.getName().equals(email)) {
            return ResponseEntity.ok(userService.findOne(email));
        } else {
            return ResponseEntity.badRequest().build();
        }

    }
}
