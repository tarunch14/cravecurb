package org.cravecurb.repository;

import java.util.List;

import org.cravecurb.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
	
	@Query("SELECT r FROM Restaurant r WHERE lower(r.name) like lower(concat('%', :context, '%')) OR lower(r.cuisineType) like lower(concat('%', :context, '%'))")
	List<Restaurant> findBySearchQuery(String context);
	
	Restaurant findByOwnerId(Long id);

}
