/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 */
public class UserService implements UserDetailsService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Userdetails service authenticating " + username);
		User user = userRepository.findByUsername(username);
		
		if(user==null) 
			throw new UsernameNotFoundException(username);
		
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), null);
	}

}
