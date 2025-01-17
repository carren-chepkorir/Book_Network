package com.example.BookNetwork.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
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
//extract username from token

@Service
public class JwtService {
    @Value("${application.security.jwt.expiration}")
    private  Long jwtExpiration;
    @Value("${application.security.jwt.secret-key}")
    public  String secretKey;
    public  String extractUserName(String token){
        return extractClaim(token, Claims::getSubject);
//        we set the subject as setSubject(userDetails.getUsername()),so Claims::getSubject wil extract username
    }

    private <T>T extractClaim(String token, Function<Claims,T>claimSolver) {
        //claimSolver determines which part of claim to extract
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
        final String username=extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token,Claims::getExpiration);
    }

    //Converts it into a cryptographic Key object used for signing and verifying tokens.
    private Key getSignInKey() {
        byte [] keyBytes= Decoders.BASE64.decode(secretKey);
            return Keys.hmacShaKeyFor(keyBytes);
    }

    ;

}
