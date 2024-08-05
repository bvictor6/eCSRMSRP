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
@RequestMapping(path = "/purchaseOrders")
public class PurchaseOrderController {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping(path = "/index")
	public String index(Model model, HttpServletRequest request) {
		model.addAttribute("invoice", "PO-001-2024/P");
		model.addAttribute("contract", "ME2/002/06/2023");
		model.addAttribute("type", "Purchase Order");
		model.addAttribute("amount", String.valueOf(1200.97));
		model.addAttribute("poDate", LocalDateTime.now());
		model.addAttribute("count", 9);
		return "purchaseOrder/index";
	}
	
	@GetMapping(path = "/view")
	public String view(Model model, HttpServletRequest request) {
		
		return "purchaseOrder/view";
	}

}
