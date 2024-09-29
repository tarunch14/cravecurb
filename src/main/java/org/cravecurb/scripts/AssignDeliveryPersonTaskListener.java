package org.cravecurb.scripts;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component("assignDeliveryPersonTaskListener")
public class AssignDeliveryPersonTaskListener implements TaskListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748711370083140399L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		
	}

}
