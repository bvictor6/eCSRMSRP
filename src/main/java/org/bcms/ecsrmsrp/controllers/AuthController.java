/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   1 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

import org.bcms.ecsrmsrp.dto.EmailVerificationDTO;
import org.bcms.ecsrmsrp.entities.EmailVerification;
import org.bcms.ecsrmsrp.repositories.EmailVerificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 */
@Controller
@RequestMapping(path = "/auth")
public class AuthController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired EmailVerificationRepository emailVerificationRepository;
	
	@GetMapping(path = "/verify/{id}")
	public String verifyEmail(@PathVariable("id") String id) {
		logger.info("decoding .. " + id);
		Base64.Decoder decoder = Base64.getDecoder();
		
		byte[] decodedBytes = decoder.decode(id);
		String verificationString  = new String(decodedBytes);
		logger.info("Decoding done...");
		logger.info(verificationString);
		
		ObjectMapper obj = new ObjectMapper();
		try {
			EmailVerificationDTO emailVerificationDTO = 
					obj.readValue(verificationString, EmailVerificationDTO.class);
			//
			logger.info("Verification token... " + emailVerificationDTO.getToken());
			EmailVerification emailVerification = getTokenObject(emailVerificationDTO);
			//update
			emailVerification.setIsEnabled(true);
			emailVerification.setIsVerified(true);
			emailVerification.setDateVerified(LocalDateTime.now());
			emailVerification.setLastModifiedBy(emailVerificationDTO.getUid());
			emailVerification.setLastModifiedDate(LocalDateTime.now());
			logger.info("Update email verification token...");
			if(emailVerificationRepository.save(emailVerification) != null) {
				return "redirect:/auth/success";
			}else {
				return "redirect:/auth/error";
			}
		} catch (JsonMappingException e) {
			e.printStackTrace();
			return "redirect:/auth/error";
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "redirect:/auth/success";
		}
		
	} 
	
	private EmailVerification getTokenObject(EmailVerificationDTO data) {
		Optional<EmailVerification> e = 
				emailVerificationRepository.findByIdAndTokenAndUserId(data.getId(), data.getToken(), data.getUid());
		if(e.isPresent() && !e.get().getIsVerified()) {
			return e.get();
		}else {
			return null;
		}
	}
	
	@GetMapping("/success")
	public String verificationSuccess() {
		return "auth/success";
	}

	@GetMapping("/error")
	public String verificationFailure() {
		return "auth/error";
	}
}
