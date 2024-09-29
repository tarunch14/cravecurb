package org.cravecurb.payload;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
	
	private long cartItemId;
	
	private Integer quantity;

}
