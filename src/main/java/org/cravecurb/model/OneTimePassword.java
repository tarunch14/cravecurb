package org.cravecurb.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OneTimePassword {
	
	@Id
	private Long id;
	
	private String otpCode;
	
	private LocalDateTime createdDate;
	
	@OneToOne
	private Customer user;

}
