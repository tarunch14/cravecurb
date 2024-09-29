package org.cravecurb.service;

import java.util.List;

import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.Category;
import org.cravecurb.model.Restaurant;
import org.cravecurb.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {
	
	private final CategoryRepository categoryRepo;
	private final RestaurantService restaurantService;
	
	public Category createCategory(String name, String jwtToken) {
		Restaurant restaurant = restaurantService.getRestaurantByUserId(jwtToken);
		Category cat = new Category();
		cat.setName(name);
		cat.setRestaurant(restaurant);
		return categoryRepo.save(cat);
	}
	
	public List<Category> findCategoryByRestaurantId(String jwtToken){
		Restaurant restaurant = restaurantService.getRestaurantByUserId(jwtToken);
		return categoryRepo.findByRestaurantId(restaurant.getId());
	}
	
	public Category findCategoryById(Long catId){
		return categoryRepo.findById(catId).orElseThrow(() -> new ResourceNotFound(String.format("category not exists with %d", catId)));
	}

}
