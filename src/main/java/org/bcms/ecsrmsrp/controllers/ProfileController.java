/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   28 Jul 2024
 */
package org.bcms.ecsrmsrp.controllers;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.classes.Results;
import org.bcms.ecsrmsrp.dto.RegistrationFormDTO;
import org.bcms.ecsrmsrp.services.CountryService;
import org.bcms.ecsrmsrp.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
@RequestMapping(path = "/profile")
public class ProfileController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired CountryService countryService;
	@Autowired ProfileService profileService;

	@GetMapping(path = "/register")
	public String create(Model model) {
		model.addAttribute("user", new RegistrationFormDTO());
		model.addAttribute("countries", countryService.getCountries());
		return "profile/add";
	}
	
	@PostMapping(path = "/save")
	public String save(@ModelAttribute RegistrationFormDTO user, HttpServletRequest request) {
		logger.info("Saving profile for " + user.getName());
		
		Results results = profileService.createUserProfile(user);
		switch (results.getStatus()) {
		case SUCCESS: {
			request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.getEmail());
			request.getSession().setAttribute(Constants._SUCCESS_MSG, results.getMessage());
			return "redirect:/profile/success";
		}
		default:
			request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.getEmail());
			request.getSession().setAttribute(Constants._ERROR_MSG, results.getMessage());
			return "redirect:/profile/error";
		}		
	}
	
	@GetMapping(path = "/success")
	public String success(HttpServletRequest request, Model model) {
		model.addAttribute("message", request.getSession().getAttribute(Constants._SUCCESS_MSG));
		model.addAttribute("email", request.getSession().getAttribute(Constants._SESSION_USER_EMAIL));
		return "/profile/success";
	}
	
	@GetMapping(path = "/error")
	public String error(HttpServletRequest request, Model model) {
		model.addAttribute("message", request.getSession().getAttribute(Constants._ERROR_MSG));
		model.addAttribute("email", request.getSession().getAttribute(Constants._SESSION_USER_EMAIL));
		return "/profile/error";
	}
}
