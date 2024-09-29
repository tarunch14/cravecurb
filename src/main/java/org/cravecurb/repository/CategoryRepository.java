package org.cravecurb.repository;

import java.util.List;

import org.cravecurb.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>{

	public List<Category> findByRestaurantId(long restId);
	
}
