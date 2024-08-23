/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import java.io.IOException;

import org.bcms.ecsrmsrp.enums.Role;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String username = request.getParameter("username");
		String remote_ip_addr = request.getRemoteAddr();
		
		logger.info(username +" :: Failed login from IP " +remote_ip_addr + " -- "+exception.getMessage());
		
		String redirectUrl = "/login?error=true&reason=" + exception.getLocalizedMessage();
		
		super.setDefaultFailureUrl(redirectUrl);
		
		super.onAuthenticationFailure(request, response, exception);
	}

}
