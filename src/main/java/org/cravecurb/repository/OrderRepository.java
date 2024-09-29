package org.cravecurb.repository;

import java.util.List;

import org.cravecurb.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{
	
	List<Order> findByCustomerId(Long userId);
	
	List<Order> findByRestaurantId(Long restaurantId);

}
