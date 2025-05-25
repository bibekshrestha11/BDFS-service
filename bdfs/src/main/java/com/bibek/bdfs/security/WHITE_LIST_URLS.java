package com.bibek.bdfs.security;

import lombok.Getter;

@Getter
public enum WHITE_LIST_URLS {
    AUTH("/api/v1/auth/**"),
    V1_ALL("/v1/**"),
    FILES("/files/**");
    private final String url;

    WHITE_LIST_URLS(String url) {
        this.url = url;
    }
}