package org.cravecurb.repository;

import java.util.List;

import org.cravecurb.model.IngredientCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientCategoryRepository extends JpaRepository<IngredientCategory, Long> {
	
	List<IngredientCategory> findByRestaurantId(Long restId);

}
