package org.cravecurb.scripts;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component("restaurantPreparationTaskListener")
public class RestaurantPreparationTaskListener implements TaskListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 771520293241528385L;

	@Override
	public void notify(DelegateTask delegateTask) {
		System.out.println("Notify kitchen !");
	}
	
}
