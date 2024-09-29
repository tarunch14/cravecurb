package org.cravecurb.controller;

import java.util.List;
import java.util.Map;

import org.cravecurb.model.Customer;
import org.cravecurb.model.CustomerReview;
import org.cravecurb.model.Order;
import org.cravecurb.payload.OrderRequest;
import org.cravecurb.service.OrderService;
import org.cravecurb.service.OrderWorkflowService;
import org.cravecurb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	private @Autowired OrderService orderService;
	private @Autowired UsersService userService;
	private @Autowired OrderWorkflowService orderWorkflowService; 
	
	@PostMapping("/add")
	@ResponseStatus(HttpStatus.CREATED)
	public String createOrder(@RequestBody OrderRequest order, @RequestHeader("Authorization") String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		return orderService.createOrder(order, user, jwtToken);
	}
	
	@GetMapping("/user")
	@ResponseStatus(HttpStatus.OK)
	public List<Order> getOrderHistory(@RequestHeader("Authorization") String jwtToken) {
		Customer user = userService.findUserByJwtToken(jwtToken);
		return orderService.getUsersOrder(user.getId());
	}
	
	@PostMapping("/workflow/{taskId}")
	@ResponseStatus(HttpStatus.OK)
	public void processWorkflow(Map<String, Object> mp, @PathVariable(value = "taskId") String tId) {
		orderWorkflowService.completeTask(mp, tId);
	}
	
	@PostMapping("/feedback")
	@ResponseStatus(HttpStatus.CREATED)
	public String submitFeedback(@RequestBody CustomerReview review) {
		return orderService.submitFeedback(review);
	}

}
