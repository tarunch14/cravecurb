package org.cravecurb.controller;

import java.util.List;

import org.cravecurb.model.IngredientCategory;
import org.cravecurb.model.IngredientsItem;
import org.cravecurb.payload.IngredientCategoryRequest;
import org.cravecurb.payload.IngredientItemRequest;
import org.cravecurb.service.IngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/ingredient")
public class IngredientController {
	
	private @Autowired IngredientsService ingredientService;
	
	@PostMapping("/category")
	@ResponseStatus(HttpStatus.CREATED)
	public IngredientCategory createIngredientCategory(@RequestBody IngredientCategoryRequest req, @RequestHeader("Authorization") String jwtToken) {
		return ingredientService.createIngredientCategory(req.getName(), req.getRestaurantId(), jwtToken);
	}
	
	@PostMapping()
	@ResponseStatus(HttpStatus.CREATED)
	public IngredientsItem createIngredientItem(@RequestBody IngredientItemRequest req, @RequestHeader("Authorization") String jwtToken) {
		return ingredientService.createIngredient(req.getRestaurantId(), req.getName(), req.getCategoryId(), jwtToken);
	}
	
	@PutMapping("update/stock/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public IngredientsItem updateStock(@PathVariable(value = "_id") Long id) {
		return ingredientService.updateStock(id);
	}
	
	@GetMapping("restaurant/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public List<IngredientsItem> getRestaurantIngredient(@PathVariable(value = "_id") Long id) {
		return ingredientService.findRestaurntIngredients(id);
	}
	
	@GetMapping("restaurant/category/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public List<IngredientCategory> getRestaurantIngredientcategory(@PathVariable(value = "_id") Long id, @RequestHeader("Authorization") String jwtToken) {
		return ingredientService.findIngredientCategoryByRestaurantId(id, jwtToken);
	}

}
