/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   Aug 31, 2024
 */
package org.bcms.ecsrmsrp.controllers;

import java.util.List;

import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 
 */
@Controller
@RequestMapping(path = "/admin")
public class AdminController {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired ProfileService profileService;
	
	@GetMapping(path = "/index")
	public String getOrgUsers(Model model, HttpServletRequest request) {
		List<User> users  = profileService.getAllOrgUsers(
					request.getSession().getAttribute("supplierCode").toString()
				);
		model.addAttribute("users", users);
		return "admin/index";
	}

}
