package com.wanted.preonboarding.util;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class UriUtil {

    public static URI createUri(String defaultUri, Long memberIdx) {

        return UriComponentsBuilder.newInstance()
                .path(defaultUri + "/{memberIdx}")
                .buildAndExpand(memberIdx)
                .toUri();
    }
}
