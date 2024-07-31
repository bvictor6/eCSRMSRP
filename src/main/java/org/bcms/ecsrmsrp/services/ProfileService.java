/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bcms.ecsrmsrp.dto.RegistrationFormDTO;
import org.bcms.ecsrmsrp.entities.VendorProfile;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.entities.UserProfile;
import org.bcms.ecsrmsrp.repositories.CountryRepository;
import org.bcms.ecsrmsrp.repositories.UserProfileRepository;
import org.bcms.ecsrmsrp.repositories.VendorProfileRepository;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
			userRepository.save(user);
			logger.info(user.getUsername() +" :: User account saved!");
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
