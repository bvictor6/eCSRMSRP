package org.bcms.ecsrmsrp.mfa.twofactorauth.controllers;

import java.io.IOException;

import org.bcms.ecsrmsrp.components.LoginFailureHandler;
import org.bcms.ecsrmsrp.components.SessionHandler;
import org.bcms.ecsrmsrp.mfa.account.Account;
import org.bcms.ecsrmsrp.mfa.account.AccountUserDetails;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthenticated;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthentication;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthenticationCodeVerifier;
import org.bcms.ecsrmsrp.mfa.twofactorauth.totp.QrCode;
import org.bcms.ecsrmsrp.services.TokenGenerationService;
import org.bcms.ecsrmsrp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TwoFactorAuthController {

	Logger logger = LoggerFactory.getLogger(getClass());
	private final UserService accountService;
	private final TokenGenerationService tokenGenerationService;
	private final SessionHandler sessionHandler;
	private final TwoFactorAuthenticationCodeVerifier codeVerifier;

	private final QrCode qrCode;

	private final AuthenticationSuccessHandler successHandler;

	private final LoginFailureHandler failureHandler;

	public TwoFactorAuthController(UserService accountService, TwoFactorAuthenticationCodeVerifier codeVerifier,
			QrCode qrCode, AuthenticationSuccessHandler successHandler, LoginFailureHandler failureHandler, 
			TokenGenerationService tokenGenerationService, SessionHandler sessionHandler) {
		this.accountService = accountService;
		this.codeVerifier = codeVerifier;
		this.qrCode = qrCode;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
		this.tokenGenerationService = tokenGenerationService;
		this.sessionHandler = sessionHandler;
	}

	@GetMapping(path = "/enable-2fa")
	public String requestEnableTwoFactor(@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		Account account = accountUserDetails.getAccount();
		String otpAuthUrl = "otpauth://totp/%s?secret=%s&digits=6".formatted("eCSRM: " + account.username(),
				account.twoFactorSecret());
		model.addAttribute("qrCode", this.qrCode.dataUrl(otpAuthUrl));
		model.addAttribute("secret", account.twoFactorSecret());
		return "auth/enable2fa";
	}

	@PostMapping(path = "/enable-2fa")
	public String processEnableTwoFactor(OtpCode otp,
			@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		logger.warn("Enable 2FA " + otp.code());
		Account account = accountUserDetails.getAccount();
		if (account.twoFactorEnabled()) {
			return "redirect:/dashboard";
		}
		if (!this.codeVerifier.verify(account, otp.code() )) {
			logger.error("invlaid code: " + otp.code());
			model.addAttribute("message", "Invalid code");
			return this.requestEnableTwoFactor(accountUserDetails, model);
		}
		Account enabled = this.accountService.enable2Fa(account);
		Authentication token = UsernamePasswordAuthenticationToken.authenticated(new AccountUserDetails(enabled), null,
				accountUserDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(token);
		return "redirect:/dashboard";
	}

	@GetMapping(path = "/challenge/totp")
	public String requestTotp(@RequestParam("otp") Boolean otp) {
		logger.info("Enter OTP Code " + otp);
		if(otp) {
			//do nothing
		}else {
			//generate otp code and send to email
			Authentication authentication = (TwoFactorAuthentication) SecurityContextHolder.getContext().getAuthentication();
			AccountUserDetails accountUserDetails = (AccountUserDetails) authentication.getPrincipal();
			String token  = tokenGenerationService.generateVerificationCode();
			logger.info("User Token generated for -- " +accountUserDetails.getUsername());
			//
			Account account =  this.accountService.update2FaToken(accountUserDetails.getAccount(), token);
			if(account == null) {
				return "redirect/login?error=true&reason=Internal error";
			}
		}
		return "auth/totp";
	}

	@PostMapping(path = "/challenge/totp")
	public void processTotp(OtpCode code, TwoFactorAuthentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Authentication auth = (TwoFactorAuthentication) SecurityContextHolder.getContext().getAuthentication();
		AccountUserDetails accountUserDetails = (AccountUserDetails) auth.getPrincipal();
		Account account = accountUserDetails.getAccount();
		
		logger.error("Verifying OTP code for ..." + account.username());	
		//
		if(code.otp())
		{
			logger.info("Verifying OTP code from authenticator app for " + account.username());
			
			if (this.codeVerifier.verify(account, code.code())) 
			{
				logger.info("OTP Code verified for " + account.username());
				TwoFactorAuthenticated twoFactorAuthenticated = new TwoFactorAuthenticated(auth);
				//
				logger.warn("Code verified " + twoFactorAuthenticated);
				SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthenticated(auth));
				this.sessionHandler.setUserSessionValues(request, account.username());//initialize session variables
				//
				this.successHandler.onAuthenticationSuccess(request, response, auth);
			}
			else {
				logger.error("Invalid code  -" + account.username());
				this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid code"));
			}
		}
		else	//email otp code 
		{
			logger.info("Verifying OTP code sent through email for " + account.username());
			if(accountService.verifyEmailToken(account, code.code)) 
			{
				logger.info("Email Code verified for " + account.username());
				TwoFactorAuthenticated twoFactorAuthenticated = new TwoFactorAuthenticated(auth);
				//
				logger.warn("Code verified " + twoFactorAuthenticated);
				SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthenticated(auth));
				this.sessionHandler.setUserSessionValues(request, account.username());//initialize session variables
				//
				this.successHandler.onAuthenticationSuccess(request, response, auth);				
			}
			else {
				logger.error("Invalid code  -" + account.username());
				this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid code"));
			}
		}
		
		
	}
	
	record OtpCode(String code, Boolean otp) {
		
	}

}
