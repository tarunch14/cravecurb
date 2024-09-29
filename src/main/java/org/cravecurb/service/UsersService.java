package org.cravecurb.service;

import java.time.LocalDateTime;
import java.util.Collection;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.cravecurb.config.JwtConstants;
import org.cravecurb.config.JwtProvider;
import org.cravecurb.exception.ResourceNotFound;
import org.cravecurb.model.Cart;
import org.cravecurb.model.Customer;
import org.cravecurb.model.OneTimePassword;
import org.cravecurb.model.USER_ROLE;
import org.cravecurb.notifications.NotificationService;
import org.cravecurb.payload.AuthResponseDto;
import org.cravecurb.payload.LoginDto;
import org.cravecurb.repository.CartRepository;
import org.cravecurb.repository.CustomerRepository;
import org.cravecurb.repository.OneTimePasswordRepository;
import org.cravecurb.utils.ServiceUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.twilio.rest.api.v2010.account.Message.Status;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
	
	private final CustomerRepository userRepo;
	private final CartRepository cartRepo;
	private final OneTimePasswordRepository otpRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtProvider;
	private final CustomUserDetailsService customUserDetailsService;
	private final NotificationService notificationService;
	
	
	public AuthResponseDto createUser(Customer user) {
		if(isDuplicateUser(user)) {
			throw new IllegalArgumentException("User already exists with provided email");
		}
		Customer newUser = new Customer();
		newUser.setEmail(user.getEmail());
		newUser.setFullName(user.getFullName());
		newUser.setRole(user.getRole());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		Customer savedUser = userRepo.save(newUser);
		
		Cart cart = new Cart();
		cart.setCustomer(savedUser);
		cart.setTotal(0L);
		cartRepo.save(cart);
		
		return setAuthorization(savedUser);
	}
	
	public Customer findUserByJwtToken(String token) {
		String email = jwtProvider.getEmailFromJwtToken(token);
		if(StringUtils.isEmpty(email)) {
			throw new IllegalArgumentException("Unauthorized access: Invalid JWT token !");
		}
		return findUserByEmail(email);
	}
	
	public Customer findUserByEmail(String email) {
		Customer user = userRepo.findByEmail(email);

		if (ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFound("user not found");
		}
		return user;
	}
	
	public AuthResponseDto setAuthorization(Customer user) {
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwtToken = jwtProvider.generateToken(authentication);
		
		AuthResponseDto authResponse = new AuthResponseDto();
		authResponse.setJwtToken(jwtToken);
		authResponse.setMessage("Registered succesfully");
		authResponse.setRole(user.getRole());
		
		return authResponse;
	}
	
	public AuthResponseDto authenticate(LoginDto dto) {
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(dto.getEmail());
		if(userDetails == null) {
			throw new IllegalArgumentException("Bad credentials");
		}
		if(! passwordEncoder.matches(dto.getPassword(), userDetails.getPassword())) {
			throw new IllegalArgumentException("Invalid password");
		}
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		String jwtToken = jwtProvider.generateToken(authentication);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		
		AuthResponseDto authResponse = new AuthResponseDto();
		authResponse.setJwtToken(jwtToken);
		authResponse.setMessage("Login succesful !");
		authResponse.setRole(USER_ROLE.valueOf(role));
		
		return authResponse;
	}
	
	public String sendOTP(String userName) {
		Customer user = userRepo.findByEmail(userName);
		if(ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFound("Incorrect userName !");
		}
		
		String otpMsg = ServiceUtils.generateOTP();
		String message = ServiceUtils.readDataFromFile("sendotp.txt");
		message.replace("{{userName}}", user.getFullName());
		message.replace("{{otp}}", otpMsg);
		Status otpStatus = notificationService.sendSmsNotification(user.getPhoneNumber(), message);
		
		OneTimePassword otpEntity = otpRepo.findByUser(user);
		if(ObjectUtils.isNotEmpty(otpEntity)) {
			otpEntity.setOtpCode(otpMsg);
			otpEntity.setCreatedDate(LocalDateTime.now());
		} else {
			otpEntity = new OneTimePassword();
			otpEntity.setUser(user);
			otpEntity.setOtpCode(otpMsg);
			otpEntity.setCreatedDate(LocalDateTime.now());
			
		}
		
		otpRepo.save(otpEntity);
		return (otpStatus.equals(Status.SENT) || otpStatus.equals(Status.DELIVERED)) ? "OTP sent sucessfully !" : "Failed to send OTP !" ;
	}
	
	public AuthResponseDto validatOTP(String userName, String otp) {
		Customer user = userRepo.findByEmail(userName);
		if(ObjectUtils.isEmpty(user)) {
			throw new ResourceNotFound("User Name does not exist !");
		}
		
		OneTimePassword otpEntity = otpRepo.findByUser(user);
		if(! otpEntity.getOtpCode().equals(otp) && otpEntity.getCreatedDate().plusMinutes(JwtConstants.OTP_VALIDITY).isAfter(LocalDateTime.now())) { //checking OTP validity
			throw new IllegalArgumentException("Invalid OTP !");
		}
		
		UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
		String jwtToken = jwtProvider.generateToken(authentication);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String role = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		
		AuthResponseDto authResponse = new AuthResponseDto();
		authResponse.setJwtToken(jwtToken);
		authResponse.setMessage("Login succesful !");
		authResponse.setRole(USER_ROLE.valueOf(role));
		
		otpRepo.deleteById(otpEntity.getId());
		
		return authResponse; 
	}
	
	public boolean isDuplicateUser(Customer newUser) {
		if (ObjectUtils.isEmpty(newUser)) return false;
		Customer existingUser = userRepo.findByEmail(newUser.getEmail());
		return existingUser != null;
	}
	
	public Customer getUserById(Long id) {
		return userRepo.findById(id).orElseThrow(() -> new ResourceNotFound("User not found with id ::" + id));
	}

}
