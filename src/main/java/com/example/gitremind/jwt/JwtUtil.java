package com.example.gitremind.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;

    /**
     * @param secretKey 를 @Value 어노테이션으로 생성자를 만들어 받아온 이유는
     * Value 가 static 변수를 초기화할 때 null로 인식되기 때문이다. 따라서 인스턴스 변수로 만들어 주었다.
     */
    public JwtUtil(@Value("${jwt.secretKey}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String subject, String type) {

        long now = System.currentTimeMillis();
        long validityTime = 3600000; // 1시간
//        long validityTime = 60000; // 1분
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

    public String createRefreshToken(String subject, String type) {

        long now = System.currentTimeMillis();
        long validityTime = 1209600000; // 2주
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

    public boolean verifyToken(String token, String type) {

        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            boolean isTypeValid = type.equals(claims.get("type", String.class));
            boolean isExpired = claims.getExpiration().before(new Date());

            return isTypeValid && !isExpired;
        } catch (Exception e) {
            // 예외 발생 시, 유효하지 않은 토큰
            return false;
        }
    }
}