package com.callicoder.config.jwt;

import com.callicoder.config.service.UserPrincipal;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpiration;

    public String generateToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        Date now = new Date();
        Date expireyDate = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                    .setSubject(Long.toString(userPrincipal.getId()))
                    .setIssuedAt(new Date())
                    .setExpiration(expireyDate)
                    .signWith(SignatureAlgorithm.HS512 , jwtSecret)
                    .compact();
    }

    public Long getUserFromToken(String token) {
        Claims claims = Jwts.parser()
                      .setSigningKey(jwtSecret)
                      .parseClaimsJws(token)
                      .getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        }catch (SignatureException e){
            logger.error("Invalid Jwt signature");
        }catch (MalformedJwtException e){
            logger.error("Invalid Jwt token");
        }catch (ExpiredJwtException e){
            logger.error("Expired Jwt token");
        }catch (IllegalArgumentException e){
            logger.error("JWT Claims string is empty");
        }
        return false;
    }
}
