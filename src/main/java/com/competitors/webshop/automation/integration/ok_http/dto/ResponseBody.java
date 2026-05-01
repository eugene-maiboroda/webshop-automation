package com.competitors.webshop.automation.integration.ok_http.dto;

public record ResponseBody<T>(int status, T body, String message) {
}
