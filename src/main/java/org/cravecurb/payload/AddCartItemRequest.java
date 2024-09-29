package org.cravecurb.payload;

import java.util.List;

import lombok.Data;

@Data
public class AddCartItemRequest {
	
	private Long foodId;
	
	private Integer quantity;
	
	private List<String> ingredients;

}
