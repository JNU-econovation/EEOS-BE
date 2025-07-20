package com.blackcompany.eeos.auth.application.domain.token;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

public class JwtTestUtil {

    private static final Date now = Date.from(Instant.now());
    private static final long accessValidTime = 3600 * 1000;
    private static final Key accessSecretKey = Keys.hmacShaKeyFor("accessSecretKey".getBytes(StandardCharsets.UTF_8));

    public static String createToken(Long memberId, String role){

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .claim("memberId", memberId)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessValidTime))
                .signWith(accessSecretKey)
                .compact();
    }

}
