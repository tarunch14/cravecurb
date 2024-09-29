package org.cravecurb.repository;

import java.util.List;

import org.cravecurb.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long>{
	
	List<Food> findByRestaurantId(Long restId);
	
	@Query("SELECT f FROM Food f WHERE f.name LIKE CONCAT('%', :keyword, '%') OR f.foodCategory.name LIKE CONCAT('%', :keyword, '%')")
	List<Food> searchFood(@Param("keyword") String keyword);


}
