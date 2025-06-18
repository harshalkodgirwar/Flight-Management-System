package com.apigateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil
{
    private static final String SECRET_KEY = "mySecretKey123yaghsdhgyuynznhjashasasyuyuyyuwuyq8787721s212xjhhd912738";

    public  static Claims extractClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token).getBody();
    }

    private static Key getSignKey(){
        byte arr[] = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(arr);
    }

    public  static boolean isTokenExpired(Claims claims){
        return claims.getExpiration().before(new Date());
    }

    public  static String getUsername(Claims claims){
        return claims.getSubject();
    }

    public  static String getRoles(Claims claims){
        return claims.get("role",String.class);
    }

}

