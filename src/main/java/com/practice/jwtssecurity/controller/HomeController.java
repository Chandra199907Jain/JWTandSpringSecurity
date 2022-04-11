package com.practice.jwtssecurity.controller;

import com.practice.jwtssecurity.model.JwtRequest;
import com.practice.jwtssecurity.model.JwtResponse;
import com.practice.jwtssecurity.service.UserService;
import com.practice.jwtssecurity.utility.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HomeController {
    @Autowired
    private JWTUtility jwtUtility;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home()
    {
        return "Welcome to OSG";
    }
    @PostMapping("/authenticate")
    public JwtResponse authenticate(@RequestBody JwtRequest jwtRequest)throws Exception
    {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            jwtRequest.getUsername(),
            jwtRequest.getPassword()
                )
        );
        }
        catch(BadCredentialsException e)
        {
            throw new Exception("Invalid Credentials");
        }

        final UserDetails userDetails
                = userService.loadUserByUsername(jwtRequest.getUsername());

        final String token
                = jwtUtility.generateToken(userDetails);

        return new JwtResponse(token);
    }
}
