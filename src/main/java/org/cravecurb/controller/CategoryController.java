package org.cravecurb.controller;

import java.util.List;

import org.cravecurb.model.Category;
import org.cravecurb.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class CategoryController {
	
	private @Autowired CategoryService categoryService;
	
	@PostMapping("/admin/category/add")
	@ResponseStatus(HttpStatus.CREATED)
	public Category createCategory(@RequestBody Category category, @RequestHeader("Authorization") String jwtToken) {
		return categoryService.createCategory(category.getName(), jwtToken);
	}
	
	@GetMapping("/category/restaurant")
	@ResponseStatus(HttpStatus.OK)
	public List<Category> getRestaurantCategories(@RequestHeader("Authorization") String jwtToken) {
		return categoryService.findCategoryByRestaurantId(jwtToken);
	}

}
