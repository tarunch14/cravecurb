package org.cravecurb.controller;

import org.cravecurb.model.Customer;
import org.cravecurb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/user")
public class UserController {
	
	private @Autowired UsersService userService;
	
	@GetMapping("/profile")
	@ResponseStatus(HttpStatus.OK)
	public Customer findUserByJwtToken(@RequestHeader(name = "Authorization", required = true) String jwt) {
		return userService.findUserByJwtToken(jwt);
	}

}
