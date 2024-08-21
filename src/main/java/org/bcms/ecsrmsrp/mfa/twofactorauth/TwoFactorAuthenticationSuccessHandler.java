package org.bcms.ecsrmsrp.mfa.twofactorauth;

import java.io.IOException;

import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.mfa.account.AccountUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TwoFactorAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	Logger logger = LoggerFactory.getLogger(getClass());

	private final AuthenticationSuccessHandler primarySuccessHandler;

	private final AuthenticationSuccessHandler secondarySuccessHandler;

	public TwoFactorAuthenticationSuccessHandler(String secondAuthUrl,
			AuthenticationSuccessHandler primarySuccessHandler) {
		logger.warn("Primary success handler " + secondAuthUrl);
		this.primarySuccessHandler = primarySuccessHandler;
		this.secondarySuccessHandler = new SimpleUrlAuthenticationSuccessHandler(secondAuthUrl);
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		logger.warn("on authentication success handler " + authentication);
		AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
		User account = accountUserDetails.getAccount();
		if (account.isTwoFactorEnabled()) {
			SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthentication(authentication));
			logger.warn("Security context holder:  " + authentication.getPrincipal().toString());
			this.secondarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
		else {
			this.primarySuccessHandler.onAuthenticationSuccess(request, response, authentication);
		}
	}

}
