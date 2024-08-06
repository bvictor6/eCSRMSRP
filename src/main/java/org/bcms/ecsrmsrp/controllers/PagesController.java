/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   25 Jul 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.util.ArrayList;
import java.util.List;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.dto.DashboardContractDTO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestClient;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
public class PagesController {
	Logger logger = LoggerFactory.getLogger(getClass());
	private RestClient restClient;
	
	@GetMapping(path = "/")
	public String index(Model model, HttpServletRequest request) {
		return "/home/index";
	}
	
	@GetMapping(path = "/dashboard")
	public String dashboard(Model model, HttpServletRequest request) {
		try 
		{
			restClient = RestClient.create();
			String results = restClient.get()
					.uri(Constants._ECSRM_BRIDGE_API + "/contracts/supplier/f54192f7-4466-46fa-9b9c-6e31670c8d35")
					.retrieve()
					.body(String.class);
			logger.info("API Response: " + results.toString());	
			int countOfCountracts = 0;
			List<DashboardContractDTO> contracts = new ArrayList<>();
			
			try {
				JSONArray jsonArray = new JSONArray(results);
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					DashboardContractDTO contract = new DashboardContractDTO();
					contract.setContractNo(jsonObject.getString("contractNo"));
					contract.setId(jsonObject.getString("id"));
					contracts.add(contract);
					countOfCountracts++;
				}
				
			}catch (Exception e) {
				logger.error(e.getLocalizedMessage());
			}
			
			//
			model.addAttribute("contractCount", countOfCountracts);
			model.addAttribute("contracts", contracts);
		}catch (Exception e) {
			logger.error(e.getLocalizedMessage());
		}
		
		return "dashboard/index";
	}
	
	@GetMapping(path = "/login")
	public String login() {
		
		return "/auth/login";
	}
	
	// Login form with error
	@GetMapping("/login-error.html")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "/auth/login";
	}

}
