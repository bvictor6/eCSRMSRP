/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   1 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.dto.EmailVerificationDTO;
import org.bcms.ecsrmsrp.entities.EmailVerification;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.EmailVerificationRepository;
import org.bcms.ecsrmsrp.services.EmailVerificationService;
import org.bcms.ecsrmsrp.services.ProfileService;
import org.bcms.ecsrmsrp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
@RequestMapping(path = "/auth")
public class AuthController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired EmailVerificationRepository emailVerificationRepository;
	@Autowired EmailVerificationService emailVerificationService;
	@Autowired ProfileService profileService;
	
	@GetMapping(path = "/verify/{id}")
	public String verifyEmail(@PathVariable("id") String id, HttpServletRequest request) {
		logger.info("decoding verification string ... " + id);
		Base64.Decoder decoder = Base64.getDecoder();
		
		byte[] decodedBytes = decoder.decode(id);
		String verificationString  = new String(decodedBytes);
		logger.info("Decoding done...");
		
		ObjectMapper obj = new ObjectMapper();
		try {
			EmailVerificationDTO emailVerificationDTO = 
					obj.readValue(verificationString, EmailVerificationDTO.class);//convert to DTO object
			//
			logger.info("Verification token... " + emailVerificationDTO.getToken());
			EmailVerification emailVerification = emailVerificationService.verifyCode(emailVerificationDTO);
			if(emailVerification != null)
			{
				//attempt to update the record as verified
				emailVerification.setIsEnabled(true);
				emailVerification.setIsVerified(true);
				emailVerification.setDateVerified(LocalDateTime.now());
				emailVerification.setLastModifiedBy(emailVerificationDTO.getUid());
				emailVerification.setLastModifiedDate(LocalDateTime.now());
				logger.info("Update email verification token...");
				//verify token
				if(emailVerificationRepository.save(emailVerification) != null) 
				{
					logger.info(emailVerification.getUserId() + "Token verification succesful!");
					Optional<User> user = profileService.loadByUserId(emailVerification.getUserId());
					if(user.isPresent()) 
					{
						if(profileService.verifyUser(user.get())) 
						{
							logger.info(user.get().getUsername() + " User account verification is successful!");
							request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.get().getUsername());
							request.getSession().setAttribute(Constants._SUCCESS_MSG, "You have successfully verified your email address: "+user.get().getUsername()+" on our servers.");
							return "redirect:/auth/success";
						}else {
							logger.error(user.get().getUsername() + " User account verification has failed, Account(User) database verification failed!");
							request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.get().getUsername());
							request.getSession().setAttribute(Constants._ERROR_MSG, "We encountered an error while verifying email address: "+user.get().getUsername()+" on our servers.");
							return "redirect:/auth/error";
						}
						
					} else {
						logger.error(user.get().getUsername() + " User account verification has failed, Account(User) not found!");
						request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.get().getUsername());
						request.getSession().setAttribute(Constants._ERROR_MSG, "We encountered an error while verifying email address: "+user.get().getUsername()+" on our servers.");
						return "redirect:/auth/error";
					}
					
				}else {
					logger.error(emailVerification.getId() + " Token verification has failed, Failed to update token!");
					request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, null);
					request.getSession().setAttribute(Constants._ERROR_MSG, "We encountered an error while verifying email address on our servers. Invalid Token!");
					return "redirect:/auth/error";
				}				
			}else {
				logger.error(emailVerification.getToken() + " Token verification has failed, Token/user combination not found!");
				request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, null);
				request.getSession().setAttribute(Constants._ERROR_MSG, "We encountered an error while verifying email address on our servers. Invalid Token!");
				return "redirect:/auth/error";
			}
			
		} catch (JsonMappingException e) {
			logger.error("Email Verification JsonMappingException :: " + e.getLocalizedMessage());
			e.printStackTrace();
			request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, null);
			request.getSession().setAttribute(Constants._ERROR_MSG, "We encountered an error while verifying email address on our servers. Error validating token!");
			return "redirect:/auth/error";
		} catch (JsonProcessingException e) {
			logger.error("Email Verification JsonProcessingException :: " + e.getLocalizedMessage());
			e.printStackTrace();
			request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, null);
			request.getSession().setAttribute(Constants._ERROR_MSG, "We encountered an error while verifying email address on our servers. Error validating token!");
			return "redirect:/auth/error";
		}
		
	} 	
		
	@GetMapping("/success")
	public String verificationSuccess(Model model, HttpServletRequest request) {
		model.addAttribute("message", request.getSession().getAttribute(Constants._SUCCESS_MSG));
		model.addAttribute("email", request.getSession().getAttribute(Constants._SESSION_USER_EMAIL));
		return "auth/success";
	}

	@GetMapping("/error")
	public String verificationFailure(Model model, HttpServletRequest request) {
		model.addAttribute("message", request.getSession().getAttribute(Constants._ERROR_MSG));
		model.addAttribute("email", request.getSession().getAttribute(Constants._SESSION_USER_EMAIL));
		return "auth/error";
	}
}
