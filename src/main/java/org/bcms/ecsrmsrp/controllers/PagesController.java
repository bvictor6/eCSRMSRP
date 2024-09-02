/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.dto.DashboardContractDTO;
import org.bcms.ecsrmsrp.services.ContractService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
public class PagesController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired ContractService contractService;
	@Autowired SessionHandler sessionHandler;
	
	@GetMapping(path = "/")
	public String index(Model model, HttpServletRequest request) {
		Principal principal = request.getUserPrincipal();
		if(principal != null)
			return "redirect:/dashboard";
		return "/home/index";
	}
	
	@GetMapping(path = "/dashboard")
	public String dashboard(Model model, HttpServletRequest request) 
	{
		Principal principal = request.getUserPrincipal();
		if(principal == null)
			return "redirect:/login";
		//
		sessionHandler.getUserSessionValues(request);
		final String supplierID = sessionHandler.getEcsrmID();
		final String user = sessionHandler.getUserName();
		logger.info(user + " dashboard access!");
		try 
		{			
			int countOfCountracts = 0;
			List<DashboardContractDTO> contracts = new ArrayList<>();
			//
			try 
			{
				String results = contractService.supplierContracts(supplierID, user);
				JSONArray jsonArray = new JSONArray(results);
				if(jsonArray.length()>0) 
				{
					for(int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						DashboardContractDTO contract = new DashboardContractDTO();
						contract.setContractNo(jsonObject.getString("contractNo"));
						contract.setId(jsonObject.getString("id"));
						contracts.add(contract);
						countOfCountracts++;
					}
				}else {
					logger.info(user + " Contract details results contained errors - " + results);
				}
				
			}catch (Exception e) {
				logger.error(user + " :: errors encountered while retrieving contract details; - " + e.getLocalizedMessage());
			}
			
			//
			model.addAttribute("contractCount", countOfCountracts);
			model.addAttribute("contracts", contracts);
		}catch (Exception e) {
			logger.error(user +" :: error processing dashboard contract details - " +e.getLocalizedMessage());
		}
		
		return "dashboard/index";
	}
	
	@GetMapping(path = "/login")
	public String login() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		logger.error("Authentication failed - " + authentication.toString());
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
        	return "/auth/login";
        }else {
        	if(authentication.isAuthenticated()) {
        		return "redirect:/dashboard";
        	}else {
        		return "/auth/login";
        	}
        }
 
        
	}
	
	// Login form with error
	@GetMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "/auth/login";
	}

}
