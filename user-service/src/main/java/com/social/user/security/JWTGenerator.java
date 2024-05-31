package com.social.user.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JWTGenerator {

	private static final Key key = getSigningKey();
	
	public String generateToken(Authentication authentication) {
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);
		
		String token = Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date())
				.setExpiration(expireDate)
				.signWith(key,SignatureAlgorithm.HS256)
				.compact();
		log.info("New token :{}",token);
		return token;
	}
	public String getUsernameFromJWT(String token){
		Claims claims = Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
		return claims.getSubject();
	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			log.error("cannot validate token -> {}",ex.getMessage());
			throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect",ex.fillInStackTrace());
		}
	}
	
    private static Key getSigningKey() {
        return Keys.hmacShaKeyFor(SecurityConstants.JWT_SECRET.getBytes());
    }

}
