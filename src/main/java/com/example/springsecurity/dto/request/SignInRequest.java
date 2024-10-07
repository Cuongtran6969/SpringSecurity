package com.example.springsecurity.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SignInRequest implements Serializable {

    @NotBlank(message = "username must be not null")
    private String userName;

    @NotBlank(message = "password must be not null")
    private String password;

    //platform, ..
}
