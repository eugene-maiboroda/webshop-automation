package com.competitors.webshop.automation.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "server")
public class AppProps {

    private int port;
    private String host;
    private Servlet servlet;

    @Getter
    @Setter
    public static class Servlet {

        private String contextPath;

    }

}