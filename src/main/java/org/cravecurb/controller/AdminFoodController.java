package org.cravecurb.controller;

import org.cravecurb.model.Food;
import org.cravecurb.payload.CreateFoodRequest;
import org.cravecurb.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {

	private @Autowired FoodService foodService;
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public Food createFood(@RequestBody CreateFoodRequest req, @RequestHeader("Authorization") String jwtToken) {
		return foodService.createFood(req, jwtToken);
	}
	
	@DeleteMapping("/delete/{_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String deleteFood(@PathVariable(value = "_id") Long id, @RequestHeader("Authorization") String jwtToken) {
		return foodService.deleteFood(id, jwtToken);
	}
	
	@PutMapping("/update/{_id}")
	@ResponseStatus(HttpStatus.CREATED)
	public Food updateFoodAvailability(@PathVariable(value = "_id") Long id, @RequestHeader("Authorization") String jwtToken) {
		return foodService.updateAvailabilityStatus(id, jwtToken);
	}
	
}
