package org.cravecurb.scripts;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component("orderCreationTaskListener")
@RequiredArgsConstructor
public class OrderCreationTaskListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8748711370083140399L;
	
    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final ObjectMapper mapper;
	

	@Override
	public void notify(DelegateTask delegatetask) {
		System.out.println("Notify restaurant"); //notification logic
		

	}

}
