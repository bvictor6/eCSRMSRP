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
@RequestMapping(path = "/payments")
public class PaymentsController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("invoice", "INV-001-2024/P");
		model.addAttribute("receipt", "RCT-001-2024/P");
		model.addAttribute("receiptAmount", String.valueOf(1200.97));
		model.addAttribute("invoiceAmount", String.valueOf(1167.00));
		model.addAttribute("receiptDate", LocalDateTime.now());
		model.addAttribute("invoiceDate", LocalDateTime.now());
		model.addAttribute("count", 5);
		return "payment/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
		return "payment/view";
	}

}
