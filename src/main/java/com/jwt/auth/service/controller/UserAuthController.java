package com.jwt.auth.service.controller;

import com.jwt.auth.service.dto.UserAuthResDTO;
import com.jwt.auth.service.dto.UserAuthDTO;
import com.jwt.auth.service.model.UserAuth;
import com.jwt.auth.service.service.UserAuthService;
import com.jwt.auth.service.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
public class UserAuthController {

    @Autowired
    private UserAuthService userAuthService;
    @Autowired
    private AuthenticationManager authenticationManager;
    //TODO: Change to bean or constructor
    private final JwtUtil jwtUtil = new JwtUtil();
    @PostMapping("/register")
    public ResponseEntity<?> registerAuth(@RequestBody UserAuthDTO userAuthDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        if (userAuthService.validateExistence(userAuthDTO.getUsername())) {
            return new ResponseEntity<>("User already exists", HttpStatus.BAD_REQUEST);
        }
        userAuthService.saveUser(userAuthDTO);
        return new ResponseEntity<>(userAuthDTO, HttpStatus.OK);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginAuth(@RequestBody UserAuthDTO userAuthDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userAuthDTO.getUsername(), userAuthDTO.getPassword()));
        if(authentication.isAuthenticated()){
            UserAuth userAuth = new UserAuth();
            userAuth.setUsername(userAuthDTO.getUsername());
            return new ResponseEntity<>(UserAuthResDTO.builder()
                    .isValidated(true)
                    .token(jwtUtil.createToken(userAuth))
                    .build(), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(UserAuthResDTO.builder()
                    .isValidated(false)
                    .token(null)
                    .build(), HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/ping")
    public String test(){
        return "pong";
    }
}
