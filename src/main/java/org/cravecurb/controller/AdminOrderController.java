package org.cravecurb.controller;

import java.util.List;

import org.cravecurb.model.Order;
import org.cravecurb.service.OrderService;
import org.cravecurb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
	
	private @Autowired OrderService orderService;
	private @Autowired UsersService userService;
	
	
	@GetMapping("/order/restaurant/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public List<Order> getOrderHistory(@PathVariable(value = "_id") Long id ,@RequestParam(required = false) String orderStatus, 
			                           @RequestHeader("Authorization") String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		return orderService.getRestautantsOrder(id, orderStatus);
	}
	
	@PutMapping("/order/{order_status}/{_id}")
	@ResponseStatus(HttpStatus.OK)
	public Order updateOrderStatus(@PathVariable(value = "_id") Long id ,
			                             @PathVariable(value = "order_status")String orderStatus, 
			                             @RequestHeader("Authorization") String jwtToken) {
		userService.findUserByJwtToken(jwtToken);
		return orderService.updateOrder(id, orderStatus);
	}

}
