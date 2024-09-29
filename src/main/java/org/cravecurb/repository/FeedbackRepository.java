package org.cravecurb.repository;

import org.cravecurb.model.CustomerReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<CustomerReview, Long> {

	
}
