package com.example.gitremind.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {

    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    public static String createToken(String subject, String type) {

        long now = System.currentTimeMillis();
        long validityTime = 3600000; // 1시간
        Date validity = new Date(now + validityTime);
        Date nowMillis = new Date(now);

        return Jwts.builder()
                .setSubject(subject)
                .claim("type", type)
                .setExpiration(validity)
                .setIssuedAt(nowMillis)
                .signWith(key)
                .compact();
    }

    public static boolean verifyToken(String token, String type) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return type.equals(claims.get("type", String.class));

        } catch (Exception e) {
            // 예외 발생 시, 유효하지 않은 토큰
            return false;
        }
    }
}