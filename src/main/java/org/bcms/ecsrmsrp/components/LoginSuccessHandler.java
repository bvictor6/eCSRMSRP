/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import java.io.IOException;
import java.time.LocalDateTime;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                  Authentication authentication) throws IOException, ServletException 
	{
		WebAuthenticationDetails d = (WebAuthenticationDetails) authentication.getDetails();
		logger.info("Succesfull login for user - " + authentication.getName() + " from " +  d.getRemoteAddress());
		logger.info("Initialize session variables for " + authentication.getName());
		
		User user  = userRepository.findByUsername(authentication.getName());
		if(user != null) {
			request.getSession().setAttribute(Constants._SESSION_USER_NAME, user.getUserProfile().getFirstname() + " " +user.getUserProfile().getLastname());
			request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, user.getUsername());
			request.getSession().setAttribute(Constants._SESSION_USER_ROLE, "Supplier");
			request.getSession().setAttribute(Constants._SESSION_USER_USER_ID, user.getId());
			request.getSession().setAttribute(Constants._SESSION_USER_ECSRM_ID, user.getVendorProfile().getEcsrmId());
			request.getSession().setAttribute(Constants._SESSION_USER_SUPPLIER_NAME, user.getVendorProfile().getName());
			request.getSession().setMaxInactiveInterval(600);
	        //update user's last login time
			user.setLastLogin(LocalDateTime.now());
			userRepository.save(user);
		}
		
		//set our response to OK status
        //response.setStatus(HttpServletResponse.SC_OK);

        //since we have created our custom success handler, its up to us, to where
        //we will redirect the user after successfully login
        SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        
		UrlPathHelper helper = new UrlPathHelper();
		String contextPath = helper.getContextPath(request);
		logger.info("context path = " + contextPath);
		String requestUrl = new String();
        if(savedRequest != null) {
        	requestUrl = savedRequest.getRedirectUrl();
        	logger.info("saved request = " + savedRequest.getRedirectUrl());
        } else {
        	requestUrl = "/dashboard";
        }
        logger.info("request url = " + requestUrl);
		
		
        response.sendRedirect(requestUrl); 
        
        //super.onAuthenticationSuccess(request, response, authentication);
    }

}
