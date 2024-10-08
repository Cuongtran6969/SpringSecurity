package com.example.springsecurity.dto.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResetPasswordDTO implements Serializable {
    private String secretKey;
    private String password;
    private String confirmPassword;
}
