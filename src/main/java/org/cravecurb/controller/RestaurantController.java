package org.cravecurb.controller;

import java.util.List;

import org.cravecurb.model.Restaurant;
import org.cravecurb.payload.RestaurantDto;
import org.cravecurb.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurant")
public class RestaurantController {
	
	private @Autowired RestaurantService restaurantService;
	
	@GetMapping("/search")
	@ResponseStatus(HttpStatus.OK)
	public List<Restaurant> searchRestaurant(@RequestParam String keyword,
			                                 @RequestHeader("Authorization") String token) {
		return restaurantService.searchRestaurant(keyword, token);
	}
	
	@GetMapping()
	@ResponseStatus(HttpStatus.OK)
	public List<Restaurant> getAllRestaurants(@RequestHeader("Authorization") String token) {
		return restaurantService.getAllRestaurants(token);
	}
	
	@GetMapping("/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public Restaurant findhRestaurantById(@PathVariable(value = "_id") Long id,
			                              @RequestHeader("Authorization") String token) {
		return restaurantService.findRestaurantById(id, token);
	}
	
	@PutMapping("/{_id}/add-favourites")
	@ResponseStatus(HttpStatus.OK)
	public RestaurantDto addToFavourites(@PathVariable(value = "_id") Long id,
			                             @RequestHeader("Authorization") String token) {
		return restaurantService.addToFavourite(id, token);
	}

}
