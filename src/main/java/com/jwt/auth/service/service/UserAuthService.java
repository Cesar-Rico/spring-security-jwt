package com.jwt.auth.service.service;

import com.jwt.auth.service.dto.UserAuthDTO;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface UserAuthService {

    void saveUser(UserAuthDTO userAuth) throws InvalidKeySpecException, NoSuchAlgorithmException;

    boolean validateExistence(String username);
}
