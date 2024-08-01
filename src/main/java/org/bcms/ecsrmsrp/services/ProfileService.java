/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bcms.ecsrmsrp.classes.MailObject;
import org.bcms.ecsrmsrp.dto.EmailVerificationDTO;
import org.bcms.ecsrmsrp.dto.RegistrationFormDTO;
import org.bcms.ecsrmsrp.entities.VendorProfile;
import org.bcms.ecsrmsrp.entities.EmailVerification;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.entities.UserProfile;
import org.bcms.ecsrmsrp.repositories.CountryRepository;
import org.bcms.ecsrmsrp.repositories.EmailVerificationRepository;
import org.bcms.ecsrmsrp.repositories.UserProfileRepository;
import org.bcms.ecsrmsrp.repositories.VendorProfileRepository;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.mail.MessagingException;

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
	@Autowired EmailVerificationService emailVerificationService;
	@Autowired EmailVerificationRepository emailVerificationRepository;
	@Autowired EmailService emailService;
	
	public void createUserProfile(RegistrationFormDTO data)  
	{
		
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
			String token = emailVerificationService.generateVerificationCode();
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
			try {
				String jsonStr = obj.writeValueAsString(emailVerificationDTO);
				logger.info(jsonStr);
				Base64.Encoder encoder = Base64.getEncoder();
				// Encoding the input string 
		        String encodedString = encoder.encodeToString(jsonStr.getBytes()); 
		        logger.info("Encoding Done: " +encodedString); 
		        //
		        //String verificationLink = Constant.PORTAL_URL + "/auth/email-verification?email=" + email + "&token=" + token;
                MailObject mailObject = new MailObject();
                mailObject.setTo(user.getUsername());
                mailObject.setRecipientName(userProfile.getFirstname() + ", " + userProfile.getLastname());
                mailObject.setSubject("Verify Your Email!");
                mailObject.setText("Please confir that you are the one registering for an account on the Supplier Portal by clicking on the link below!");
                mailObject.setTemplateEngine("email");
                mailObject.setSenderName("eCSRM System.");
                mailObject.setVerificationLink("http://127.0.0.1:8089/auth/verify/"+encodedString);

                logger.info("Send verification email to " + user.getUsername());

                Map<String, Object> templateModel = new HashMap<>();
                templateModel.put("recipientName", mailObject.getRecipientName());
                templateModel.put("text", mailObject.getText());
                templateModel.put("senderName", mailObject.getSenderName());
                templateModel.put("copyrightYear", mailObject.getMailCopyrightYear());
                templateModel.put("verificationLink", mailObject.getVerificationLink());

                if (mailObject.getTemplateEngine().equalsIgnoreCase("email")) 
                {
                    logger.info("Email schedular task execute send email cron job for - " + mailObject.getTemplateEngine().toUpperCase());
                    try {
                        emailService.sendMessageUsingThymeleafTemplate(
                                mailObject.getTo(),
                                mailObject.getSubject(),
                                templateModel);
                        logger.info(mailObject.getTemplateEngine().toUpperCase() + ": Cron job successfully sent email to " + mailObject.getTo() + " at " + LocalDateTime.now() + ", with subject " + mailObject.getSubject());
                    } catch (IOException e) {
                        logger.error(mailObject.getTemplateEngine().toUpperCase() + ": Cron job failed to send email to " + mailObject.getTo() + " at " + LocalDateTime.now() + ", with subject " + mailObject.getSubject() + " due to IOException " + e.getLocalizedMessage());
                        e.printStackTrace();
                    } catch (MessagingException e) {
                        logger.error(mailObject.getTemplateEngine().toUpperCase() + ": Cron job failed to send email to " + mailObject.getTo() + " at " + LocalDateTime.now() + ", with subject " + mailObject.getSubject() + " due to MessagingException " + e.getLocalizedMessage());
                        e.printStackTrace();
                    }
                }
			}catch (Exception e) {
				e.printStackTrace();
			}
			
		}else {
			//exception
		}
	}
	
	private Boolean userExists(String username) {
		if(userRepository.findByUsername(username) != null) {
			return true;
		}else {
			return false;
		}
	}
	
	private VendorProfile setProfileValues(RegistrationFormDTO data) {
		VendorProfile vendorProfile = new VendorProfile();
		//
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

}
