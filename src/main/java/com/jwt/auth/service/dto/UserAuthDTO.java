package com.jwt.auth.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class UserAuthDTO implements Serializable {
    private String username;
    private String password;
}
