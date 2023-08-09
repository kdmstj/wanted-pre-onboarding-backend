package com.wanted.preonboarding.domain.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PostDto {

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post{
        private String content;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Response {
        private Long postIdx;
        private String content;
    }

    @Builder @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Patch {
        private String content;
    }
}
