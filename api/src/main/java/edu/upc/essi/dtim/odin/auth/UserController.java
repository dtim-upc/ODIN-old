package edu.upc.essi.dtim.odin.auth;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

//    private final UserDetailsService userDetailsService;
//
//    public UserController(UserDetailsService userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @GetMapping
//    public UserDetails getUser(Authentication authentication) {
//        System.out.println("GET USER...");
//        JwtAuthenticationToken token = (JwtAuthenticationToken) authentication;
//        Map<String, Object> attributes = token.getTokenAttributes();
//        return userDetailsService.loadUserByUsername(attributes.get("username").toString());
//    }
}