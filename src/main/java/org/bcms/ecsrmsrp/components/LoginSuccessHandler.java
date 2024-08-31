/**
 * Author: B. Victor
 * E-Mail: bvictor@ymail.com
 * Date:   7 Aug 2024
 */
package org.bcms.ecsrmsrp.components;

import java.io.IOException;
import java.util.Optional;

import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthentication;
import org.bcms.ecsrmsrp.repositories.UserRepository;
import org.bcms.ecsrmsrp.services.TokenGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 */
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());
	private final UserRepository userRepository;
	@Autowired TokenGenerationService tokenGenerationService;
	private final LoginFailureHandler failureHandler;
	private final AuthenticationSuccessHandler primarySuccessHandler;	
	private AuthenticationSuccessHandler secondarySuccessHandler;
	
	/**
	 * 
	 */
	public LoginSuccessHandler(String secondAuthUrl, 
			AuthenticationSuccessHandler primarySuccessHandler, UserRepository userRepository,
			LoginFailureHandler failureHandler) {
		logger.warn("Primary success handler " + secondAuthUrl);
		this.primarySuccessHandler = primarySuccessHandler;
		this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler(secondAuthUrl);
		this.userRepository = userRepository;
		this.failureHandler = failureHandler;
	}
	
	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, 
                  Authentication authentication) throws IOException, ServletException 
	{
		WebAuthenticationDetails d = (WebAuthenticationDetails) authentication.getDetails();
		logger.info("Succesfull login for user - " + authentication.getName() + " from " +  d.getRemoteAddress());
				
		Optional<User> user  = userRepository.findByUsername(authentication.getName());
		
		if(user.isPresent()) 
		{
			User u = user.get();			
			if(u.getTwoFactorSecret() == null) 
			{
				try 
				{
					logger.info(u.getUsername() + " - Enabling 2FA !");
					u.setTwoFactorSecret(TimeBasedOneTimePasswordUtil.generateBase32Secret());
					userRepository.save(u);
				}catch (Exception e) {
					logger.error(u.getUsername() + " :: Error, unable to set 2FA secret! - " + e.getLocalizedMessage());;
				}
				
			}
			//
			if(u.getIsVerified())
			{
				if(u.getIsEnabled())
				{
					//Two factor stuff
					logger.error("2FA Enabled: " +u.getTwoFactorEnabled());
					if(u.getTwoFactorEnabled()) 
					{
						SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
						logger.warn("Security context holder:  " + authentication.getPrincipal().toString());
						this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
					} else {
						SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
						logger.warn("Security context holder:  " + authentication.getPrincipal().toString());
						logger.error("Two factor authentication disabled for user " + authentication);
						//force user to enroll
						this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler("/enable-2fa");
						this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
					}
				}
				else //user is not enabled, logout user
				{
					logger.error(u.getUsername() + " Account not enabled - signing out user!");
					SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
					this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Account Disabled!"));				
				}
			}
			else
			{
				logger.error(u.getUsername() + " Account not verified - signing out user!");
				SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
				this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Account Not Verified, Check email to verify your accout!"));				
			}
		}
		
    }

}
