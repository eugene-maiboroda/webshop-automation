package com.competitors.webshop.automation.integration.ok_http.okhttp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class OkHttpService {

    private final OkHttpClient okHttpClient;

    public OkResponseBody execute(Request request) {
        try (Response response = okHttpClient.newCall(request).execute()) {
            okhttp3.ResponseBody body = response.body();

            String responseBody = Objects.requireNonNull(body).string();

            if (!response.isSuccessful()) {
                String message = responseBody.isEmpty() ? "HTTP error " + response.code() : responseBody;
                return OkResponseBody.builder().code(response.code()).body(message).build();
            }

            return OkResponseBody.builder().code(response.code()).body(responseBody).build();
        } catch (IOException e) {
            return OkResponseBody.builder()
                    .code(500)
                    .body("Request failed: " + e.getMessage())
                    .build();
        }
    }
}