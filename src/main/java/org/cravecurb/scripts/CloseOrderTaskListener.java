package org.cravecurb.scripts;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component("closeOrderTaskListener")
public class CloseOrderTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1125509403623368668L;

	@Override
	public void notify(DelegateTask delegateTask) {
		// TODO Auto-generated method stub
		
	}

}
