/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 18, 2024
 */
package org.bcms.ecsrmsrp.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bcms.ecsrmsrp.classes.Results;
import org.bcms.ecsrmsrp.entities.LoginToken;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.LoginTokenRepository;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 */
@Service
public class AuthLoginTokenService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;
	@Autowired LoginTokenRepository loginTokenRepository;
	@Autowired ComposeEmailService composeEmailService;
	
	public Results createLoginToken(String token, String userId) 
	{
		Results results = new Results();
		LoginToken loginToken = new LoginToken();
		User user = userRepository.findById(UUID.fromString(userId)).get();
		//
		loginToken.setToken(token);
		loginToken.setCreatedBy(UUID.fromString(userId));
		loginToken.setDateCreated(LocalDateTime.now());
		loginToken.setIsEnabled(true);
		loginToken.setIsValid(true);
		loginToken.setUserId(UUID.fromString(userId));
		logger.info("OTP created succesfully for " + user.getUsername());
		if(loginTokenRepository.save(loginToken) != null) 
		{
			logger.info("Send OTP email for " + user.getUsername());
			String subject = "eCSRM Login OTP";
			String body = "Please provide the below OTP code in order to login: ";
			results = composeEmailService.composeEmailMessage(user.getUsername(), 
            		user.getUserProfile().getFirstname() + " " + user.getUserProfile().getLastname(),
            		subject,
            		body,
            		"");			
		}
		return results;
	}

}
