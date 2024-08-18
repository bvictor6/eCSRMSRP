/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   1 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.dto.TokenDTO;
import org.bcms.ecsrmsrp.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
@RequestMapping(path = "/auth")
public class AuthController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired AuthenticationService authenticationService;	
	
	/**
	 * Verify user email upon registration
	 * @param id
	 * @param request
	 * @return
	 */
	@GetMapping(path = "/verify/{id}")
	public String verifyEmail(@PathVariable("id") String id, HttpServletRequest request) {
		logger.info("decoding verification string ... " + id);
		
		return authenticationService.verifyUserEmail(id, request);
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
	
	@GetMapping(path = "/otp")
	public String otp(HttpServletRequest request) {
		
		return "auth/otp";
	}
	
	@PostMapping(path = "/otp/verify")
	public String verifyOtp(HttpServletRequest request, @ModelAttribute TokenDTO token) {
		
		
		return null;
	}
}
