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
        @Email(message = "이메일 형식이 맞지 않습니다.")
        private String email;

        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        private String password;
    }
}
