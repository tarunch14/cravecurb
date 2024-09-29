package org.cravecurb.payload;

import lombok.Data;

@Data
public class IngredientCategoryRequest {
	
	private String name;
	
	private Long restaurantId;

}
