/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Sep 3, 2024
 */
package org.bcms.ecsrmsrp.api.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.bcms.ecsrmsrp.api.interfaces.UserDetailsInterface;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 
 */
@RestController
@RequestMapping(path = "/api/v1")
public class UserController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;

	@GetMapping(path = "/users/{code}")
	public ResponseEntity<?> getNewUsersByVendor(@PathVariable("code") String code){
		
		List<UserDetailsInterface> users = userRepository.findBySupplierCodeAndIsEnabled(code, false);
		
		return new ResponseEntity<>(users, HttpStatus.OK);
	}
	
	@GetMapping(path = "/user/{id}/{status}")
	public ResponseEntity<?> updateUserDetails(@PathVariable("id") String id, @PathVariable("status") Boolean status){
		Optional<User> user  = userRepository.findById(UUID.fromString(id));
		//logger.info("Update user status for " + userDTO.getId());
		if(user.isPresent()) 
		{
			logger.info("Update user status for " + user.get().getUsername());
			User u = user.get();
			u.setIsEnabled(status);
			userRepository.save(u);
			
			return new ResponseEntity<>("User updated!", HttpStatus.OK);
		}else {
			logger.info("Failed to update user status for " + user.get().getUsername());
			return new ResponseEntity<>("Error updating user details!", HttpStatus.NOT_FOUND);
		}
	}
}
