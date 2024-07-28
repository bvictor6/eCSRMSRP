/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   28 Jul 2024
 */
package org.bcms.ecsrmsrp.controllers;

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

	@GetMapping(path = "/create")
	public String create(Model model) {
		
		return "profile/add";
	}
}
