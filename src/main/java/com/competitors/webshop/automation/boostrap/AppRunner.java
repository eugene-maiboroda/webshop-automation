package com.competitors.webshop.automation.boostrap;

import com.competitors.webshop.automation.config.AppProps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

     private final AppProps appProps;
    private static final String URL_TEMPLATE = "http://%s:%d%s/swagger-ui/index.html";

    @Override
    public void run(@NotNull ApplicationArguments args) {
        log.info("Swagger UI: {}", URL_TEMPLATE.formatted(appProps.getHost(), appProps.getPort(), appProps.getServlet().getContextPath()));
    }
}
