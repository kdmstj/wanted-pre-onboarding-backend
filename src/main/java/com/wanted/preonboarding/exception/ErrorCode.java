package com.wanted.preonboarding.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /*
    400 BAD REQUEST : 잘못된 요청
     */


    /*
    401 UNAUTHORIZED : 인증되지 않은 사용자
     */
    INVALID_USER(UNAUTHORIZED, "등록된 회원 정보가 없습니다."),
    INVALID_AUTH_TOKEN(UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    /*
    404 NOT_FOUND : Resource를 찾을 수 없음
     */
    POST_NOT_FOUND(NOT_FOUND, "해당 게시물을 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(NOT_FOUND, "해당 유저 정보를 찾을 수 없습니다");
    /*
    409 CONFLICT : Resource의 현재 상태와 충돌 (중복된 데이터 존재)
     */
    private final HttpStatus httpStatus;
    private final String detail;
}
