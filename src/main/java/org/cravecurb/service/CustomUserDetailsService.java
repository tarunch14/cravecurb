package org.cravecurb.service;

import java.util.ArrayList;
import java.util.List;

import org.cravecurb.model.Customer;
import org.cravecurb.model.USER_ROLE;
import org.cravecurb.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private @Autowired CustomerRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer user = userRepo.findByEmail(username);
		if(user == null) {
			throw new UsernameNotFoundException(String.format("user %s not found with provided email", username));
		}
		USER_ROLE role = user.getRole();
		if(role == null) {
			role = USER_ROLE.ROLE_CUSTOMER;
		}
		
//		   User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
//	                 .orElseThrow(() ->
//	                         new UsernameNotFoundException("User not found with username or email: "+ usernameOrEmail));
//
//	        Set<GrantedAuthority> authorities = user
//	                .getRoles()
//	                .stream()
//	                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(role.toString()));
		
		return new User(user.getEmail(), user.getPassword(), authorities);
	}

}
