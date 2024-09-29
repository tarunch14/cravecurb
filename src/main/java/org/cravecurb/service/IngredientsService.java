package org.cravecurb.service;

import java.util.List;

import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.IngredientCategory;
import org.cravecurb.model.IngredientsItem;
import org.cravecurb.model.Restaurant;
import org.cravecurb.repository.IngredientCategoryRepository;
import org.cravecurb.repository.IngredientItemRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IngredientsService {
	
	private final IngredientCategoryRepository ingCatRrepo;
	private final IngredientItemRepository ingItemRepo;
	private final RestaurantService restaurantService;
	
	public IngredientCategory createIngredientCategory(String name, Long restId, String jwtToken) {
		Restaurant rest = restaurantService.findRestaurantById(restId, jwtToken);
		
		IngredientCategory ingredientCategory = new IngredientCategory();
		ingredientCategory.setName(name);
		ingredientCategory.setRestaurant(rest);
		return ingCatRrepo.save(ingredientCategory);
	}
	
	public IngredientCategory findIngredientCategoryById(Long ingId) {
		return ingCatRrepo.findById(ingId).orElseThrow(() -> new ResourceNotFound(String.format("Ingredient category not exists with %d", ingId)));
	}
	
	public List<IngredientCategory> findIngredientCategoryByRestaurantId(Long restId, String jwtToken) {
		restaurantService.findRestaurantById(restId, jwtToken);
		return ingCatRrepo.findByRestaurantId(restId);
	}
	
	public IngredientsItem createIngredient(Long restId, String ingredienttName, Long categoryId, String jwtToken) {
		Restaurant rest = restaurantService.findRestaurantById(restId, jwtToken);
		IngredientCategory ingCategory = findIngredientCategoryById(categoryId);
		
		IngredientsItem ingItem = new IngredientsItem();
		ingItem.setName(ingredienttName);
		ingItem.setRestaurant(rest);
		ingItem.setCategory(ingCategory);
		
		IngredientsItem item = ingItemRepo.save(ingItem);
		ingCategory.getIngredients().add(item);
		return item;
	}
	
	public List<IngredientsItem> findRestaurntIngredients(Long restId){
		return ingItemRepo.findByRestaurantId(restId);
	}
	
	public IngredientsItem updateStock(Long id) {
		IngredientsItem ingItem = ingItemRepo.findById(id).orElseThrow(() -> new ResourceNotFound(String.format("Ingredient not exists with %d", id)));
		ingItem.setAvailability(! ingItem.getAvailability());
		
		return ingItemRepo.save(ingItem);	
	}

}
