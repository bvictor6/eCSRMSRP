/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.bcms.ecsrmsrp.classes.Results;
import org.bcms.ecsrmsrp.dto.EmailVerificationDTO;
import org.bcms.ecsrmsrp.dto.RegistrationFormDTO;
import org.bcms.ecsrmsrp.entities.EmailVerification;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.entities.UserProfile;
import org.bcms.ecsrmsrp.entities.VendorProfile;
import org.bcms.ecsrmsrp.enums.ResultStatus;
import org.bcms.ecsrmsrp.repositories.CountryRepository;
import org.bcms.ecsrmsrp.repositories.EmailVerificationRepository;
import org.bcms.ecsrmsrp.repositories.UserProfileRepository;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.bcms.ecsrmsrp.repositories.VendorProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

/**
 * 
 */
@Service
public class ProfileService {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired CountryRepository countryRepository;
	@Autowired VendorProfileRepository vendorProfileRepository;
	@Autowired UserProfileRepository userProfileRepository;
	@Autowired UserRepository userRepository;
	@Autowired PasswordEncoder passwordEncoder;
	@Autowired TokenGenerationService tokenGenerationService;
	@Autowired EmailVerificationRepository emailVerificationRepository;
	@Autowired ComposeEmailService composeEmailService;
	
	public Results createUserProfile(RegistrationFormDTO data)  
	{
		Results results = new Results();
		
		VendorProfile vendorProfile = setProfileValues(data);
		
		UserProfile userProfile = setUserProfile(data);
		
		User user = new User();
		user.setUsername(data.getUsername());
		user.setPassword(passwordEncoder.encode(data.getPassword()));
		user.setDateCreated(LocalDateTime.now());
		user.setTermsAccepted(data.getTerms());
		user.setIsActive(false);
		user.setIsEnabled(false);
		user.setIsLocked(false);
		user.setIsVerified(false);
		user.setVendorProfile(vendorProfile);
		user.setLastModifiedDate(LocalDateTime.now());
		user.setTwoFactorSecret(TimeBasedOneTimePasswordUtil.generateBase32Secret());
		user.setTwoFactorEnabled(false);
		//
		if(!userExists(user.getUsername())) 
		{
			vendorProfile = vendorProfileRepository.save(vendorProfile);
			logger.info(user.getUsername() +" :: Vendor profile for " + vendorProfile.getName() + " saved!");
			user.setVendorProfile(vendorProfile);
			//
			userProfile = userProfileRepository.save(userProfile);
			logger.info(user.getUsername() +" :: User profile for " + userProfile.getFirstname() + "" + userProfile.getLastname() + " saved!");
			user.setUserProfile(userProfile);
			//
			logger.info(user.getUsername() + " :: Save user account details!");
			user  = userRepository.save(user);
			logger.info(user.getUsername() +" :: User account saved!");
			//Verify by email token
			String token = tokenGenerationService.generateVerificationCode();
			EmailVerification emailVerification = new EmailVerification();
			emailVerification.setToken(token);
			emailVerification.setUserId(user.getId());
			emailVerification.setDateCreated(LocalDateTime.now());
			emailVerification.setIsEnabled(true);
			emailVerification.setIsVerified(false);
			
			emailVerification = emailVerificationRepository.save(emailVerification);
			EmailVerificationDTO emailVerificationDTO = new EmailVerificationDTO();
			emailVerificationDTO.setId(emailVerification.getId());
			emailVerificationDTO.setToken(emailVerification.getToken());
			emailVerificationDTO.setUid(emailVerification.getUserId());
			//
			ObjectMapper obj = new ObjectMapper();
			try 
			{
				String jsonStr = obj.writeValueAsString(emailVerificationDTO);
				logger.info(jsonStr);
				Base64.Encoder encoder = Base64.getEncoder();
				// Encoding the input string 
		        String encodedString = encoder.encodeToString(jsonStr.getBytes()); 
		        logger.info("Encoding Done: " +encodedString); 
		        //
		        String verificationLink = "http://127.0.0.1:8089/auth/verify/"+encodedString;
		        String subject = "Verify Your Email!";
		        String body = "Please confir that you are the one registering for an account on the Supplier Portal by clicking on the link below!";
               results = composeEmailService.composeEmailMessage(user.getUsername(), 
                		userProfile.getFirstname() + " " + userProfile.getLastname(),
                		subject,
                		body,
                		verificationLink);
                //
			}catch (Exception e) {
				logger.error(user.getUsername() + " :: error creating account - " + e.getLocalizedMessage());
				e.printStackTrace();
				results.setStatus(ResultStatus.ERROR);
                results.setMessage("An error was encountered while creating your account with message:  " + e.getLocalizedMessage());
                return results;
			}
			
		}else {
			//exception
			logger.error(user.getUsername() + " is already registered! Aborting profile registration..." );
			results.setStatus(ResultStatus.ERROR);
            results.setMessage(user.getUsername() + " is already registered!");
            return results;
		}
		return results;
	}
	
	private Boolean userExists(String username) {
		if(userRepository.findByUsername(username) != null) {
			logger.error(username + " exists, abort creating new profile!");
			return true;
		}else {
			logger.info(username + " does not exist, creating new profile!");			
			return false;
		}
	}
	
	private VendorProfile setProfileValues(RegistrationFormDTO data) {
		VendorProfile vendorProfile = new VendorProfile();
		//
		vendorProfile.setEcsrmId(data.getEcsrmId());
		vendorProfile.setContractNo(data.getContractNo());
		vendorProfile.setTenderNo(data.getTenderNo());
		vendorProfile.setCountry(countryRepository.findById(UUID.fromString(data.getCountry())).get());
		vendorProfile.setDateCreated(LocalDateTime.now());
		vendorProfile.setEmail(data.getEmail());
		vendorProfile.setFax(data.getFax());
		vendorProfile.setIsEnabled(false);
		vendorProfile.setLastModifiedDate(LocalDateTime.now());
		vendorProfile.setMobileNo(data.getMobile());
		vendorProfile.setName(data.getName());
		vendorProfile.setPhone(data.getPhone());
		vendorProfile.setPhysicalAddress(data.getPhysicalAddress());
		vendorProfile.setPostalAddress(data.getPostalAddress());
		//
		return vendorProfile;		
	}
	
	private UserProfile setUserProfile(RegistrationFormDTO data) {
		UserProfile profile = new UserProfile();
		//
		profile.setAddress(data.getContactPersonAddress());
		profile.setDateCreated(LocalDateTime.now());
		profile.setFirstname(data.getFirstname());
		profile.setIsEnabled(false);
		profile.setLastModifiedDate(LocalDateTime.now());
		profile.setLastname(data.getLastname());
		profile.setPhone(data.getPhone());
		//
		return profile;
	}
	
	public Optional<User> loadByUserId(UUID id) {
		return userRepository.findById(id);
	}
	
	public Boolean verifyUser(User user) {
		user.setIsVerified(true);
		user.setIsActive(true);
		user.setLastModifiedDate(LocalDateTime.now());
		if(userRepository.save(user) != null) {
			return true;
		}else {
			return false;
		}
	}

}
