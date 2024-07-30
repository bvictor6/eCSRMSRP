/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Jul 30, 2024
*/
package org.bcms.ecsrmsrp.services;

import java.time.LocalDateTime;
import java.util.UUID;

import org.bcms.ecsrmsrp.dto.RegistrationFormDTO;
import org.bcms.ecsrmsrp.entities.Profile;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.CountryRepository;
import org.bcms.ecsrmsrp.repositories.ProfileRepository;
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
	@Autowired ProfileRepository profileRepository;
	@Autowired UserRepository userRepository;
	@Autowired PasswordEncoder passwordEncoder;
	
	public void createUserProfile(RegistrationFormDTO data)  {
		
		Profile profile = setProfileValues(data);
		
		User user = new User();
		user.setUsername(profile.getEmail());
		user.setPassword(passwordEncoder.encode(data.getPassword()));
		user.setDateCreated(LocalDateTime.now());
		user.setIsActive(false);
		user.setIsEnabled(false);
		user.setIsLocked(false);
		user.setIsVerified(false);
		user.setProfile(profile);
		user.setLastModifiedDate(LocalDateTime.now());
		//
		if(profileExists(profile)) {
			profile = profileRepository.save(profile);
			logger.info("Profile saved!");
			user.setProfile(profile);
			//
			logger.info("Save user...");
			userRepository.save(user);
			logger.info("user saved!");
		}else {
			//exception
		}
	}
	
	private Boolean profileExists(Profile profile) {
		if(profileRepository.findByEmail(profile.getEmail()) != null) {
			return false;
		} else {
			return true;
		}
	}
	
	private Profile setProfileValues(RegistrationFormDTO data) {
		Profile profile = new Profile();
		//
		profile.setContactPerson(data.getFirstName() + " " + data.getLastName() );
		profile.setContactPersonDesignation(data.getDesignation());
		
		profile.setCountry(countryRepository.findById(UUID.fromString(data.getCountry())).get());
		profile.setDateCreated(LocalDateTime.now());
		profile.setEmail(data.getEmail());
		profile.setFax(data.getFax());
		profile.setIsEnabled(false);
		profile.setLastModifiedDate(LocalDateTime.now());
		profile.setMobileNo(data.getMobile());
		profile.setName(data.getName());
		profile.setPhone(data.getPhone());
		profile.setPhysicalAddress(data.getPhysicalAddress());
		profile.setPostalAddress(data.getPostalAddress());
		profile.setTermsAccepted(data.getTerms());
		//
		return profile;		
	}

}
