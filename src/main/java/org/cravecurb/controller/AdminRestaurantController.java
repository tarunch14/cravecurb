package org.cravecurb.controller;

import org.cravecurb.model.Restaurant;
import org.cravecurb.payload.CreateRestaurantRequest;
import org.cravecurb.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/admin/restaurant")
public class AdminRestaurantController {
	
	private @Autowired RestaurantService restaurantService;
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public Restaurant createRestaurant(@RequestBody CreateRestaurantRequest restReq,
			                           @RequestHeader("Authorization") String token) {
		return restaurantService.createRestaurant(restReq, token);
	}
	
	@PutMapping("/update/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public Restaurant updateRestaurant(@RequestBody CreateRestaurantRequest restReq,
			                           @PathVariable(value = "_id") Long id,
			                           @RequestHeader("Authorization") String token) {
		return restaurantService.updateRestaurant(restReq, id, token);
	}
	
	@DeleteMapping("/delete/{_id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public String updateRestaurantStatus(@PathVariable(value = "_id") Long id,
			                             @RequestHeader("Authorization") String token) {
		return restaurantService.deleteRestaurant(id, token);
	}
	
	@PutMapping("/status/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public Restaurant createRestaurant(@PathVariable(value = "_id") Long id,
			                           @RequestHeader("Authorization") String token) {
		return restaurantService.updateRestaurantStatus(id, token);
	}
	
	
	@GetMapping("/view")
	@ResponseStatus(HttpStatus.OK)
	public Restaurant findRestaurantByUserId(@RequestHeader("Authorization") String token) {
		return restaurantService.getRestaurantByUserId(token);
	}
	

}
