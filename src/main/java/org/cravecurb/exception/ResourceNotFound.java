package org.cravecurb.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFound extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5619002270355408865L;
	
	private String message;
	
	public ResourceNotFound(String message){
		super();
		this.message=message;
	}

}
