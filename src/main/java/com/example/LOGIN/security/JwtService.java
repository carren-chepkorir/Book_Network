package com.example.LOGIN.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.impl.crypto.MacProvider;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.var;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
//generate,decode,validate Token
//do anything related to Token

@Service
public class JwtService {
    @Value("${application.security.jwt.expiration}")
    private  Long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    public  String secretKey;
    public  String extractUserName(String token){
        return extractCLaim(token, Claims::getSubject);
    }

    private <T>T extractCLaim(String token, Function<Claims,T>claimSolver) {
           final Claims claims=extractAllCLaims(token);
            return claimSolver.apply(claims);
    }

    private Claims extractAllCLaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(),userDetails);
    };
    public String generateToken(Map<String,Object> claims,UserDetails userDetails){
        return buildToken(claims,userDetails,jwtExpiration);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            Long jwtExpiration) {
        var authorities=userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+jwtExpiration))
                .claim("authorities",authorities)
                .signWith(getSignInKey())
                .compact()
                ;
    }
    public  boolean isTokenValid(String token,UserDetails userDetails){
        final String username=extractUserName();
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractCLaim(token,Claims::getExpiration);
    }

    private Key getSignInKey() {
        byte [] keyBytes= Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
    }

    ;

}
