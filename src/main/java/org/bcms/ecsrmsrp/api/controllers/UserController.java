/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Sep 3, 2024
 */
package org.bcms.ecsrmsrp.api.controllers;

import java.util.List;

import org.bcms.ecsrmsrp.api.interfaces.UserDetailsInterface;
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
}
