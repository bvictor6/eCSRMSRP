package org.bcms.mfa.twofactorauth;

import org.bcms.mfa.account.Account;

public interface TwoFactorAuthenticationCodeVerifier {

	boolean verify(Account account, String code);

}
