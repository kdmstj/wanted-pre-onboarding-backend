package com.wanted.preonboarding.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LoginDto {
    @Email
    private String email;
    @Size(min = 8)
    private String password;
}
