package org.cravecurb.payload;

import java.util.List;

import org.cravecurb.model.Category;
import org.cravecurb.model.IngredientsItem;

import lombok.Data;

@Data
public class CreateFoodRequest {
	
	private String name;
	private String description;
	private Long price;
	private Category foodCategory;
	private List<String> images;
	private boolean available;
	private Long restaurantId;
	private boolean isVegetarian;
	private boolean isSeasonable;
	private List<IngredientsItem> ingredients;

}
