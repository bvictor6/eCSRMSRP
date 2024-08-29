/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bcms.ecsrmsrp.classes.Results;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.enums.Role;
import org.bcms.ecsrmsrp.mfa.account.Account;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;

/**
 * 
 */
@Service
public class UserService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;
	@Autowired HttpServletRequest request;
	@Autowired ComposeEmailService composeEmailService;

	
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
	
	@Transactional
	public Account enable2Fa(Account account) 
	{
		Optional<User> user = userRepository.findByUsername(account.username());
		
		if(user.isPresent()) {
			User u = user.get();
			u.setTwoFactorEnabled(true);
			//
			userRepository.save(u);
			return new Account(u.getUsername(), u.getPassword(), u.getTwoFactorSecret(), false);
		}else {
			return account;
		}		
	}
	
	@Transactional
	public Account update2FaToken(Account account, String token) {
		Optional<User> user = userRepository.findByUsername(account.username());
		
		if(user.isPresent()) 
		{
			User u = user.get();
			u.setTwoFactorToken(token);
			u.setTwoFactorTime(LocalDateTime.now());
			userRepository.save(u);
			//
			String template = "token";
	        String subject = "Verify Your Email!";
	        String body = "Your authentication code to login to the  Supplier Portal is " + token + ". The code is valid for 5 minutes!";
	        Results results = composeEmailService.composeEmailMessage(u.getUsername(), 
            		u.getUserProfile().getFirstname() + " " + u.getUserProfile().getLastname(),
            		subject,
            		body,
            		"",
            		template);
	        logger.info(results.getMessage());
			return new Account(u.getUsername(), u.getPassword(), u.getTwoFactorSecret(), false);
		}else {
			return null;
		}				
	}
	
	public Boolean verifyEmailToken(Account account, String token) {
		Optional<User> user = userRepository.findByUsernameAndTwoFactorToken(account.username(), token);
		if(user.isPresent()) 
		{
			User u = user.get();
			LocalDateTime now = LocalDateTime.now();
			Long diff  = ChronoUnit.MINUTES.between(LocalDateTime.from(u.getTwoFactorTime()), now);
			logger.info(account.username() + " verification token - " + token + " is " + diff + " minutes old!");
			if(diff <= 5) {
				logger.info(account.username() + " - token is valid - " + token);
				u.setTwoFactorToken(null);
				u.setTwoFactorTime(null);
				userRepository.save(u);
				return true;
			}else {
				logger.error(account.username() + " - token expired - " + token);
				return false;
			}
		}else {
			logger.error(account.username() + " - failed token validation - " + token);
			return false;
		}
		
		
	}
		
}
