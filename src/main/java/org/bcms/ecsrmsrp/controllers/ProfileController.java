/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   28 Jul 2024
 */
package org.bcms.ecsrmsrp.controllers;

import org.bcms.ecsrmsrp.services.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 */
@Controller
@RequestMapping(path = "/profile")
public class ProfileController {
	@Autowired CountryService countryService;

	@GetMapping(path = "/register")
	public String create(Model model) {
		model.addAttribute("countries", countryService.getCountries());
		return "profile/add";
	}
}
