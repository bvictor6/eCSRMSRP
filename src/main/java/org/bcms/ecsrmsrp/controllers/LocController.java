/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   4 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */

@Controller
@RequestMapping(path = "/loc")
public class LocController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("contract", "DS2/010/05/2023");
		model.addAttribute("loc", "CMS/LOC/435/2023-2024");
		model.addAttribute("issueDate", LocalDateTime.now());
		model.addAttribute("count", 15);
		return "loc/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
		return "loc/view";
	}

}
