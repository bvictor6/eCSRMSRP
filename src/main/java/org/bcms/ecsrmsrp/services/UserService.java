/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.enums.Role;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;
	@Autowired HttpServletRequest request;

	
	public User findByUsername(String username) throws UsernameNotFoundException {
		logger.info("Userdetails service authenticating " + username);
		Optional<User> user = userRepository.findByUsername(username);
		
		if(user.isEmpty()) 
			throw new UsernameNotFoundException(username);
		
		List<String> authorities = new ArrayList<>();
		Role role = Role.SUPPLIER;
		authorities.add(role.toString());
		
		return user.get();
	}
		
}
