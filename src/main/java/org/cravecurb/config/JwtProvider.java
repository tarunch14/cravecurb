package org.cravecurb.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Service
public class JwtProvider {

//	@Value("${spring.application.jwt-secret-key}")
//	private String jwtSecretKey;
	
//	@Value("${spring.application.jwt-expiration-milliseconds}")
//	private String expireDate;
	
	private SecretKey key;

	@PostConstruct
	public void init() {
		key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(JwtConstants.SECRET_KEY));
	}

	public String generateToken(Authentication auth) {
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		String roles = populateAuthorities(authorities);
		 String token = Jwts.builder()
		            .setIssuedAt(Date.from(Instant.now()))
		            .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
		            .claim("email", auth.getName())
		            .claim("authorities", roles)
		            .signWith(key)
		            .compact();
		
		return token;
	}

	private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
		Set<String> auths = new HashSet<String>();
		for(var authority : authorities) {
			auths.add(authority.getAuthority());
		}
		return String.join(",", auths);
	}
	
	public String getEmailFromJwtToken(String jwtToken) {
		Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwtToken.substring(7)).getBody();
		return String.valueOf(claims.get("email"));
		
	}
}
