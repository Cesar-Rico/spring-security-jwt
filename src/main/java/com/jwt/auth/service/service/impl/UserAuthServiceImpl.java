package com.jwt.auth.service.service.impl;

import com.jwt.auth.service.dto.UserAuthDTO;
import com.jwt.auth.service.model.UserAuth;
import com.jwt.auth.service.repository.UserAuthRepository;
import com.jwt.auth.service.service.UserAuthService;
import com.jwt.auth.service.util.Utils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Service
@AllArgsConstructor
public class UserAuthServiceImpl implements UserAuthService {

    private UserAuthRepository userAuthRepository;
    @Override
    public void saveUser(UserAuthDTO userAuthDTO) throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] salt = Utils.generateSalt();
        UserAuth userAuth = UserAuth.builder()
                .username(userAuthDTO.getUsername())
                .password(Utils.generateHash(userAuthDTO.getPassword(), salt))
                .build();
        try {
            userAuthRepository.save(userAuth);
        } catch (Exception e) {
            //TODO: Agregar logger
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateExistence(String username) {
        return userAuthRepository.existsById(username);
    }
}
