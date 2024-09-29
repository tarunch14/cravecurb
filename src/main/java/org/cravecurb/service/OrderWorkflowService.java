package org.cravecurb.service;


import java.util.Map;

import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderWorkflowService {
	
	private final TaskService taskService;
	
	
	public void completeTask(Map<String, Object> variable, String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new IllegalArgumentException("Task not found for taskId: " + taskId);
		}
		
		taskService.complete(taskId, variable);
	}

}
