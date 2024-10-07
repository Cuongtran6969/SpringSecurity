package com.example.springsecurity.Service.impl;

import com.example.springsecurity.Service.JwtService;
import com.example.springsecurity.util.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.antlr.v4.runtime.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImp implements JwtService {

//    @Value("${jwt.expiryTime}")
    private long expiryTime = 1;

    private long expiryDay = 14;

//    @Value("${jwt.secretKey}")
    private String secretkey = "803ccb9767523d85485cb85e6310e73b34929e7df240d51fb5c9606a9cd94148";

    private String refreshkey = "04efcb14e6e81940dde1c386ad894d63fc5428a3749185aa70b1e5b82faf8ed7";

   @Override
    public String generateToken(UserDetails user) {
        return generateToken(new HashMap<>(), user);
    }

    @Override
    public String generateRefreshToken(UserDetails user) {
        return generateRefreshToken(new HashMap<>(), user);
    }

    @Override
    public String extractUsername(String token, TokenType type) {
        return extractClaims(token, type, Claims::getSubject);
    }

    @Override
    public boolean isValid(String token, TokenType type, UserDetails userDetails) {
        final String username = extractUsername(token, type);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token, type);
    }

    private boolean isTokenExpired(String token, TokenType type) {
        return extractExpiration(token, type).before(new Date());
    }

    private Date extractExpiration(String token, TokenType type) {
        return extractClaims(token, type, Claims::getExpiration);
    }
    private String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)//thong tin bi mat khong public (ma hoa thong tin)
                .setSubject(userDetails.getUsername())//khong trung lap
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + 1000*60*60*expiryTime)))
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)//dinh nghia thuat toan
                .compact();
    }

    private String generateRefreshToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(claims)//thong tin bi mat khong public (ma hoa thong tin)
                .setSubject(userDetails.getUsername())//khong trung lap
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date((System.currentTimeMillis() + 1000*60*60*24*expiryDay)))
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)//dinh nghia thuat toan
                .compact();
    }


    private Key getKey(TokenType type) {
        byte[] keyBytes;
        if(TokenType.ACCESS_TOKEN.equals(type)) {
            keyBytes = Decoders.BASE64.decode(secretkey);//ma hoa secrekey va giai ma
        } else {
            keyBytes = Decoders.BASE64.decode(refreshkey);//ma hoa secrekey va giai ma
        }
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaims(String token, TokenType type, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token, type);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token, TokenType type) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey(type))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


}
