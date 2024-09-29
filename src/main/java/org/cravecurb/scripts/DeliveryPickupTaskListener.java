package org.cravecurb.scripts;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component("deliveryPickupTaskListener")
public class DeliveryPickupTaskListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1151092008858796731L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		
	}

}
