package com.jwt.auth.service.util;

import com.jwt.auth.service.model.UserAuth;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;

import java.util.Date;
import java.util.List;

public class JwtUtil {
    private final String secret_key = "carnix_secret_key";
    //private long accessTpkenValidity = 5 * 60 * 60;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer ";

    public JwtUtil() {
        jwtParser = Jwts.parser().setSigningKey(secret_key);
    }

    public String createToken(UserAuth userAuth){
        Claims claims = Jwts.claims().setSubject(userAuth.getUsername());
        Date tokenCreateTime = new Date();
        //Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(tokenCreateTime)
                //.setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }
    public Claims parseJwtClaims(String token){
        return jwtParser.parseClaimsJws(token).getBody();
    }
    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
        }
        return null;
    }
    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
    public boolean validateClaims(Claims claims) throws AuthenticationException {
        try {
            return claims.getExpiration().after(new Date());
        } catch (Exception e) {
            throw e;
        }
    }
    public String getUser(Claims claims) {
        return claims.getSubject();
    }

    private List<String> getRoles(Claims claims) {
        return (List<String>) claims.get("roles");
    }
}
