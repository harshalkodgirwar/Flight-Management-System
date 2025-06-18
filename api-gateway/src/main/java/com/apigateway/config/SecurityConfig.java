package com.apigateway.config;

import com.apigateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig
{
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        System.out.println("Enter in SecurityWebFilterChain");
        return http.authorizeExchange(exchange -> exchange
                        .pathMatchers("/auth/register",
                                "/auth/users",
                                "/auth/login").permitAll()
                        .pathMatchers("/flight/**").hasRole("ADMIN")
                        .pathMatchers("/booking/**").hasAnyRole("ADMIN","USER")
                   //     .pathMatchers("/guest/**").hasRole("OWNER")
                        .anyExchange().authenticated())
                .addFilterAt(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf(csrfSpec -> csrfSpec.disable())
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }
}