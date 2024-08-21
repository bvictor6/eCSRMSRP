/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

import org.bcms.ecsrmsrp.classes.Constants;
import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.enums.Role;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.bcms.ecsrmsrp.services.AuthLoginTokenService;
import org.bcms.ecsrmsrp.services.TokenGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
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
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired UserRepository userRepository;
	@Autowired TokenGenerationService tokenGenerationService;
	@Autowired AuthLoginTokenService authLoginTokenService;
	
	private final AuthenticationSuccessHandler primarySuccessHandler;

	private final AuthenticationSuccessHandler secondarySuccessHandler;
	
	/**
	 * 
	 */
	public LoginSuccessHandler(String secondAuthUrl, AuthenticationSuccessHandler primarySuccessHandler) {
		logger.warn("Primary success handler " + secondAuthUrl);
		this.primarySuccessHandler = primarySuccessHandler;
		this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler(secondAuthUrl);
	}
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                  Authentication authentication) throws IOException, ServletException 
	{
		WebAuthenticationDetails d = (WebAuthenticationDetails) authentication.getDetails();
		logger.info("Succesfull login for user - " + authentication.getName() + " from " +  d.getRemoteAddress());
		logger.info("Initialize session variables for " + authentication.getName());
		
		Optional<User> user  = userRepository.findByUsername(authentication.getName());
		if(user.isPresent()) {
			User u = user.get();
			request.getSession().setAttribute(Constants._SESSION_USER_NAME, u.getUserProfile().getFirstname() + " " +u.getUserProfile().getLastname());
			request.getSession().setAttribute(Constants._SESSION_USER_EMAIL, u.getUsername());
			request.getSession().setAttribute(Constants._SESSION_USER_ROLE, Role.GUEST); //set to guest mode until OTP verification is done
			request.getSession().setAttribute(Constants._SESSION_USER_USER_ID, u.getId());
			request.getSession().setAttribute(Constants._SESSION_USER_ECSRM_ID, u.getVendorProfile().getEcsrmId());
			request.getSession().setAttribute(Constants._SESSION_USER_SUPPLIER_NAME, u.getVendorProfile().getName());
			request.getSession().setMaxInactiveInterval(900);//15min
	        //update user's last login time
			u.setLastLogin(LocalDateTime.now());
			userRepository.save(u);
		}
		
		//set our response to OK status
        //response.setStatus(HttpServletResponse.SC_OK);

        //since we have created our custom success handler, its up to us, to where
        //we will redirect the user after successfully login
        /*SavedRequest savedRequest = new HttpSessionRequestCache().getRequest(request, response);
        
		UrlPathHelper helper = new UrlPathHelper();
		String contextPath = helper.getContextPath(request);
		logger.info("context path = " + contextPath);
		String requestUrl = new String();
		if(request.getSession().getAttribute(Constants._SESSION_USER_ROLE).equals(Role.GUEST)) 
		{
			requestUrl = "/auth/otp?otp=true&email=" + request.getSession().getAttribute(Constants._SESSION_USER_EMAIL);
			String token = tokenGenerationService.generateVerificationCode();
			//
			authLoginTokenService.createLoginToken(token, 
					request.getSession().getAttribute(Constants._SESSION_USER_USER_ID).toString());			
		}else {
			if(savedRequest != null) {
	        	requestUrl = savedRequest.getRedirectUrl();
	        	logger.info("saved request = " + savedRequest.getRedirectUrl());
	        } else {
	        	requestUrl = "/dashboard";
	        }
		}
        
        logger.info("request url = " + requestUrl);
		
		
        response.sendRedirect(requestUrl); */
        
        //super.onAuthenticationSuccess(request, response, authentication);
		
		//Two factor stuff
		if(user.get().isTwoFactorEnabled()) {
			logger.error("Two factor authentication enabled for user " + authentication);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		} else {
			logger.error("Two factor authentication disabled for user " + authentication);
			//force user to enroll
			//this.primarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
			response.sendRedirect("/enable-2fa");
		}
		
		
    }

}
