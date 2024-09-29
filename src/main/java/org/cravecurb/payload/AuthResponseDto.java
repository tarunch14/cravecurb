package org.cravecurb.payload;

import org.cravecurb.model.USER_ROLE;

import lombok.Data;

@Data
public class AuthResponseDto {
	
	private String jwtToken;
	
	private String message;
	
	private USER_ROLE role;

}
