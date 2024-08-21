package org.bcms.ecsrmsrp.mfa.twofactorauth.controllers;

import java.io.IOException;

import org.bcms.ecsrmsrp.mfa.account.Account;
import org.bcms.ecsrmsrp.mfa.account.AccountUserDetails;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthenticated;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthentication;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthenticationCodeVerifier;
import org.bcms.ecsrmsrp.mfa.twofactorauth.totp.QrCode;
import org.bcms.ecsrmsrp.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class TwoFactorAuthController {

	Logger logger = LoggerFactory.getLogger(getClass());
	private final UserService accountService;

	private final TwoFactorAuthenticationCodeVerifier codeVerifier;

	private final QrCode qrCode;

	private final AuthenticationSuccessHandler successHandler;

	private final AuthenticationFailureHandler failureHandler;

	public TwoFactorAuthController(UserService accountService, TwoFactorAuthenticationCodeVerifier codeVerifier,
			QrCode qrCode, AuthenticationSuccessHandler successHandler, AuthenticationFailureHandler failureHandler) {
		this.accountService = accountService;
		this.codeVerifier = codeVerifier;
		this.qrCode = qrCode;
		this.successHandler = successHandler;
		this.failureHandler = failureHandler;
	}

	@GetMapping(path = "/enable-2fa")
	public String requestEnableTwoFactor(@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		Account account = accountUserDetails.getAccount();
		String otpAuthUrl = "otpauth://totp/%s?secret=%s&digits=6".formatted("Demo: " + account.username(),
				account.twoFactorSecret());
		model.addAttribute("qrCode", this.qrCode.dataUrl(otpAuthUrl));
		model.addAttribute("secret", account.twoFactorSecret());
		return "enable2fa";
	}

	@PostMapping(path = "/enable-2fa")
	public String processEnableTwoFactor(OtpCode otp,
			@AuthenticationPrincipal AccountUserDetails accountUserDetails, Model model) {
		logger.warn("Enable 2FA " + otp.code());
		Account account = accountUserDetails.getAccount();
		if (account.twoFactorEnabled()) {
			return "redirect:/";
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
		return "redirect:/";
	}

	@GetMapping(path = "/challenge/totp")
	public String requestTotp() {
		logger.info("Entefr OTP Code");
		return "totp";
	}

	@PostMapping(path = "/challenge/totp")
	public void processTotp(OtpCode code, TwoFactorAuthentication authentication,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		//TwoFactorAuthentication twoFactorAuthentication =  
		logger.error("OTP Challenge..."+ code.code() + " -- ");		
		//
		Authentication auth = (TwoFactorAuthentication) SecurityContextHolder.getContext().getAuthentication();
		String username = auth.getName();
		logger.error("OTP Challenge..."+ code.code() + " -- 2 " + auth);
		//
		//Authentication primaryAuthentication = (Authentication) auth.getPrincipal();
		//
		AccountUserDetails accountUserDetails = (AccountUserDetails) auth.getPrincipal();
		logger.error("OTP Challenge..."+ code.code() + " -- 3 " + accountUserDetails);
		Account account = accountUserDetails.getAccount();
		if (this.codeVerifier.verify(account, code.code())) {
			logger.warn("Code verified!");
			TwoFactorAuthenticated twoFactorAuthenticated = new TwoFactorAuthenticated(auth);
			//
			logger.warn("Code verified " + twoFactorAuthenticated);
			SecurityContextHolder.getContext().setAuthentication(new TwoFactorAuthenticated(auth));
			this.successHandler.onAuthenticationSuccess(request, response, auth);
		}
		else {
			logger.error("Invalid code.." + code.code());
			this.failureHandler.onAuthenticationFailure(request, response, new BadCredentialsException("Invalid code"));
		}
	}
	
	record OtpCode(String code) {
		
	}

}
