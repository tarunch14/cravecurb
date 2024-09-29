package org.cravecurb.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.Food;
import org.cravecurb.model.Restaurant;
import org.cravecurb.payload.CreateFoodRequest;
import org.cravecurb.repository.FoodRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodService {
	
	private final FoodRepository foodRepo;
	private final ModelMapper modelMapper;
	private final UsersService userService;
	private final RestaurantService restaurantService;

	public Food createFood(CreateFoodRequest req, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		Restaurant rest = restaurantService.findRestaurantById(req.getRestaurantId(), jwtToken);
		
		Food food = modelMapper.map(req, Food.class);
		food.setFoodCategory(req.getFoodCategory());
		rest.getFood().add(food);
		return foodRepo.save(food);
	}
	
	public String deleteFood(Long foodId, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		Food food = foodRepo.findById(foodId).orElseThrow(()-> new ResourceNotFound(String.format("food not found with %d", foodId)));
		food.setRestaurant(null);
		foodRepo.save(food);
		return "food deleted successfully !";
	}
	
	public List<Food> getRestaurantsFood(Long restaurantId, boolean isVeg, boolean isNonVeg, boolean isSeasonal, String foodCategory, String jwtToken){
		userService.findUserByJwtToken(jwtToken);
		
		List<Food> foods = foodRepo.findByRestaurantId(restaurantId);
		if(isVeg)
			foods = filterByVeg(foods, isVeg);
		if(isNonVeg)
			foods = filterByNonVeg(foods, isNonVeg);
		if(isSeasonal)
			foods = filterBySeasonal(foods, isSeasonal);
		if(ObjectUtils.isEmpty(foodCategory))
			foods = filterByCategory(foods, foodCategory);
		
		return foods;
	}
	
	private List<Food> filterByCategory(List<Food> foods, String foodCategory) {
		return foods.stream().filter(
				food -> (ObjectUtils.isEmpty(food) && food.getFoodCategory().getName().equalsIgnoreCase(foodCategory)))
				.collect(Collectors.toList());
	}

	private List<Food> filterBySeasonal(List<Food> foods, boolean isSeasonal) {
		return foods.stream().filter((food) -> food.getIsSeasonable()==isSeasonal).collect(Collectors.toList());
	}

	private List<Food> filterByNonVeg(List<Food> foods, boolean isNonVeg) {
		return foods.stream().filter((food) -> food.getIsVegetarian()==false).collect(Collectors.toList());
	}

	private List<Food> filterByVeg(List<Food> foods, boolean isVeg) {
		return foods.stream().filter((food) -> food.getIsVegetarian()==isVeg).collect(Collectors.toList());
	}

	public List<Food> searchFood(String keyword, String jwtToken){
		userService.findUserByJwtToken(jwtToken);
		return foodRepo.searchFood(keyword);
	}

	public Food findFoodById(Long foodId) {
		return foodRepo.findById(foodId).orElseThrow(() -> new ResourceNotFound(String.format("food not exists with %d", foodId)));
	}
	
	public Food updateAvailabilityStatus(Long foodId, String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		Food food = foodRepo.findById(foodId).orElseThrow(() -> new ResourceNotFound(String.format("food not exists with %d", foodId)));
		food.setAvailable(!food.getAvailable());
		return foodRepo.save(food);
	}
	
}
