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
@RequestMapping(path = "/deliveries")
public class DeliveryController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("contract", "CMS/007/019/2024");
		model.addAttribute("loc", "TYR-723432");
		model.addAttribute("sku", "CUP005001");
		model.addAttribute("location", "BK-01-72-06-01");
		model.addAttribute("quantity", 300);
		model.addAttribute("unit", "Prefill Syringe");
		model.addAttribute("class", 600);
		model.addAttribute("description", "Delivery of KWALITY-AMPICILLIN 500MG POWDER FOR INJECTION, 1VIAL");
		model.addAttribute("deliveryDate", LocalDateTime.now());
		model.addAttribute("count", 15);
		return "delivery/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
		return "delivery/view";
	}
	
	@GetMapping(path = "/schedule")
	public String schedule(Model model, HttpServletRequest request) {
		
		return "/delivery/schedule";
	}

}
