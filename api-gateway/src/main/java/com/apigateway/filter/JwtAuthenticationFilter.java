package com.apigateway.filter;

import com.apigateway.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter implements WebFilter
{

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);

            try{
                var claims = JwtUtil.extractClaims(token);

                if(!JwtUtil.isTokenExpired(claims)){
                    String username = JwtUtil.getUsername(claims);
                    String role = JwtUtil.getRoles(claims);
                    System.out.println("Username: "+ JwtUtil.getUsername(claims));
                    System.out.println("Role: "+JwtUtil.getRoles(claims));

                    var authorities = Arrays.stream(new String[] {role})
                            .map(r -> new SimpleGrantedAuthority("ROLE_"+r))
                            .collect(Collectors.toList());

                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username,role,authorities);

                    SecurityContext securityContext = new SecurityContextImpl(authenticationToken);

                    ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(exchange.getRequest()){
                        @Override
                        public HttpHeaders getHeaders(){
                            HttpHeaders headers = new HttpHeaders();
                            headers.putAll(super.getHeaders());
                            headers.add("loggedInUser",username);
                            return headers;
                        }
                    };

                    ServerWebExchange mutatedExChange = exchange.mutate().request(mutatedRequest).build();

                    return chain.filter(mutatedExChange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                }
            }
            catch (Exception e){
                e.printStackTrace();
                return Mono.error(new RuntimeException("invalid JWT token"));
            }

        }
        return chain.filter(exchange);
    }
}

