package org.cravecurb.controller;

import org.cravecurb.model.Customer;
import org.cravecurb.payload.AuthResponseDto;
import org.cravecurb.payload.LoginDto;
import org.cravecurb.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	private @Autowired UsersService userService;
	
	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponseDto createUserHandler(@RequestBody @Valid Customer user){
		return userService.createUser(user);
	}
	
	@PostMapping("/login")
	@ResponseStatus(HttpStatus.OK)
	public AuthResponseDto signIn(@RequestBody LoginDto login) {
		return userService.authenticate(login);	
	}
	
	@GetMapping("otp/generate.login/{_user}")
	@ResponseStatus(HttpStatus.OK)
	public String sendOTP(@PathVariable(value = "_user") String userName) {
		return userService.sendOTP(userName);
	}
	
	@PostMapping("login/otp")
	@ResponseStatus(HttpStatus.OK)
	public AuthResponseDto signInWithOTP(@RequestParam String userName, @RequestParam String otp) {
		return userService.validatOTP(userName, otp);
	}

}
