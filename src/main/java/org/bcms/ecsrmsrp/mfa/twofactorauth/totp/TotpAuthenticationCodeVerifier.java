package org.bcms.ecsrmsrp.mfa.twofactorauth.totp;

import java.security.GeneralSecurityException;

import org.bcms.ecsrmsrp.mfa.account.Account;
import org.bcms.ecsrmsrp.mfa.twofactorauth.TwoFactorAuthenticationCodeVerifier;
import org.springframework.util.StringUtils;

import com.j256.twofactorauth.TimeBasedOneTimePasswordUtil;

public class TotpAuthenticationCodeVerifier implements TwoFactorAuthenticationCodeVerifier {

	@Override
	public boolean verify(Account account, String code) {
		try {
			return TimeBasedOneTimePasswordUtil.validateCurrentNumber(account.twoFactorSecret(),
					StringUtils.hasText(code) ? Integer.parseInt(code) : 0, 10000);
		}
		catch (GeneralSecurityException e) {
			throw new IllegalStateException(e);
		}
	}

	

}
