package org.cravecurb.repository;

import org.cravecurb.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>{
	
	@Transactional
	public Customer findByEmail(String userName);

}
