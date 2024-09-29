package org.cravecurb.repository;

import org.cravecurb.model.Customer;
import org.cravecurb.model.OneTimePassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OneTimePasswordRepository extends JpaRepository<OneTimePassword, Long> {
	
	OneTimePassword findByUser(Customer user);
	
}
