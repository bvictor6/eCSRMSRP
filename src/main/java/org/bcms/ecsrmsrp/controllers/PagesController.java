/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
public class PagesController {
	
	@GetMapping(path = "/")
	public String Home(Model model, HttpServletRequest request) {
		
		return "home/index";
	}

}
