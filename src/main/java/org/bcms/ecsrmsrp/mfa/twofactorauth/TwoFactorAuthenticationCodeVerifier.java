package org.bcms.ecsrmsrp.mfa.twofactorauth;

import org.bcms.ecsrmsrp.mfa.account.Account;

public interface TwoFactorAuthenticationCodeVerifier {

	boolean verify(Account account, String code);

}
