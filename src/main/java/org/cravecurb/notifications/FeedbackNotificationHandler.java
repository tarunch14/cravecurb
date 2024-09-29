package org.cravecurb.notifications;

import java.util.HashMap;
import java.util.Map;

import org.cravecurb.config.SpringContextHelper;
import org.cravecurb.model.Customer;
import org.cravecurb.service.UsersService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FeedbackNotificationHandler implements JavaDelegate {
	
	private final String subject = "Order review";
	private final String templateName = "feedback.html";
	private final UsersService userService;

	@Override
	public void execute(DelegateExecution execution) {
		NotificationService notificationservice = SpringContextHelper.getBean(NotificationService.class);
		sendFeedbackLink(execution, notificationservice);
		
	}
	
	private void sendFeedbackLink(DelegateExecution execution, NotificationService emailService) {
		Map<String, Object> context = new HashMap<String, Object>();
		context.put("userName", execution.getVariable("author"));
		context.put("articleName", execution.getVariable("articleName"));
		
		Customer user = userService.getUserById((Long) execution.getVariable("customer_id"));
		emailService.sendEmailNotification(user.getEmail(), subject, templateName, context);
	}

}
