package org.bcms.ecsrmsrp.mfa.twofactorauth;

import org.bcms.ecsrmsrp.entities.User;

public interface TwoFactorAuthenticationCodeVerifier {

	boolean verify(User account, String code);

}
