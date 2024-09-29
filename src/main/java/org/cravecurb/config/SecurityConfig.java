package org.cravecurb.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	    http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
	        .authorizeHttpRequests(authorize -> authorize
	            .requestMatchers("/api/admin/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN")
	            .requestMatchers("/auth/**").permitAll()  // Allow register without authentication
	            .requestMatchers("/auth/**").authenticated()
	            .requestMatchers("/api/**").authenticated()
	            .anyRequest().permitAll()
	        ).addFilterBefore(new JwtTokenValidator(), BasicAuthenticationFilter.class)
	         .csrf(csrf -> csrf.disable())
	         .cors(cors -> cors.configurationSource(corsConfigurationSource()));

	    return http.build();
	}


    
	private CorsConfigurationSource corsConfigurationSource() {
		return new CorsConfigurationSource() {

			@Override
			public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
				CorsConfiguration ccfg = new CorsConfiguration();
		        ccfg.setAllowedOrigins(Arrays.asList("https://zomato-food.versal.app", "http://localhost:5454", "*"));
		        ccfg.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
		        ccfg.setAllowCredentials(true);
		        ccfg.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type", "X-Requested-With", "Accept", "Origin"));
		        ccfg.setExposedHeaders(Arrays.asList("Authorization"));
		        ccfg.setMaxAge(3600L);
				return ccfg;
			}
		};

	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	
}

