/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.util.ArrayList;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
public class UserService implements UserDetailsService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;
	@Autowired HttpServletRequest request;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Userdetails service authenticating " + username);
		User user = userRepository.findByUsername(username);
		
		if(user==null) 
			throw new UsernameNotFoundException(username);
		
		logger.info(username + " ...initialize session variables");
		
		request.getSession().setAttribute(Constants._SESSION_USER_NAME, user.getProfile().getName());
		request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.getUsername());
		request.getSession().setAttribute(Constants._SESSION_USER_ROLE, "Supplier");
		request.getSession().setAttribute(Constants._SESSION_USER_USER_ID, user.getId());
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}

}
