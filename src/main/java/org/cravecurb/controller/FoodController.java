package org.cravecurb.controller;

import java.util.List;

import org.cravecurb.model.Food;
import org.cravecurb.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/food")
public class FoodController {
	
	private @Autowired FoodService foodService;
	
	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Food> serachFood(@RequestParam String name, @RequestHeader("Authorization") String jwtToken) {
		return foodService.searchFood(name, jwtToken);
	}
	
	@GetMapping("/restaurant/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public List<Food> getRestaurantFood(@PathVariable(value = "_id") Long restId, @RequestParam boolean isVeg,
			                            @RequestParam boolean isNonVeg, @RequestParam boolean isSeasonal,
			                            @RequestParam(required = false) String foodCategory,
			                            @RequestHeader("Authorization") String jwtToken) {
		return foodService.getRestaurantsFood(restId, isVeg, isNonVeg, isSeasonal, foodCategory, jwtToken);
	}

}
