/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   3 Aug 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.time.LocalDate;
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
@RequestMapping(path = "/contracts")
public class ContractController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("contract", "ME2/002/06/2023");
		model.addAttribute("tender", "SUP 6/13/23");
		model.addAttribute("type", "Framework");
		model.addAttribute("category", "Work");
		model.addAttribute("amount", String.valueOf(1200.97));
		model.addAttribute("contractDate", LocalDate.parse("2022-10-17"));
		model.addAttribute("count", 15);
		return "contract/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
		return "/contracts/view";
	}

}
