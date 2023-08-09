package com.wanted.preonboarding.domain.member.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class MemberDto {

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {

        @NotBlank
        @Email
        private String email;

        @Size(min = 8)
        private String password;
    }
}
