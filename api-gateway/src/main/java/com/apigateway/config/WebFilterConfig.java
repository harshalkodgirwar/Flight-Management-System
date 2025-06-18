package com.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.springframework.web.server.WebFilter;

@Configuration
public class WebFilterConfig implements WebFluxConfigurer {

    private static final String SECRET_KEY = "mySecretKey123yaghsdhgyuynznhjashasasyuyuyyuwuyq8787721s212xjhhd912738";


    @Bean
    public WebFilter additionalFilter() {
        return (exchange, chain) -> {
            // Add custom filter logic here
            return chain.filter(exchange);
        };
    }

//    @Bean
//    public ReactiveJwtDecoder reactiveJwtDecoder() {
//        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//        SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
//        return NimbusReactiveJwtDecoder.withSecretKey(secretKey).build();
//    }
}