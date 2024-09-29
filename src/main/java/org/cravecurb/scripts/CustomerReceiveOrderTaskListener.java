package org.cravecurb.scripts;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@Service
@Component("customerReceiveOrderTaskListener")
public class CustomerReceiveOrderTaskListener implements TaskListener { //send feedback link

	/**
	 * 
	 */
	private static final long serialVersionUID = -8767160416603156128L;

	@Override
	public void notify(DelegateTask delegateTask) {
		
		
	}	


}
