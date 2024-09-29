package org.cravecurb.scripts;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component("feedbackTaskListener")
public class FeedbackTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -940933918308950632L;
	
	

	@Override
	public void notify(DelegateTask delegateTask) {
		
		
	}

}
