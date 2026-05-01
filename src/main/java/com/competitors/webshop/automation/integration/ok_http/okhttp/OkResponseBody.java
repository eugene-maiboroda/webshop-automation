package com.competitors.webshop.automation.integration.ok_http.okhttp;

import lombok.Builder;

@Builder
public record OkResponseBody(
        int code,
        String body,
        String massage
) {
    public boolean isSuccessful() {
        return code >= 200 && code <= 299;
    }

}