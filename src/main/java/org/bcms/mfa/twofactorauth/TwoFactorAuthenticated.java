/*
* Author: B. Victor
* E-Mail: bvictor@ymail.com
* Date:   Aug 20, 2024
*/
package org.bcms.mfa.twofactorauth;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.CredentialsContainer;

/**
 * 
 */
public class TwoFactorAuthenticated extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	private final Authentication primary;

	/**
	 * @param authorities
	 */
	public TwoFactorAuthenticated(Authentication primary) {
		super(List.of());
		this.primary = primary;
	}

	@Override
	public Object getCredentials() {
		return this.primary.getCredentials();
		
	}

	@Override
	public Object getPrincipal() {
		return this.primary.getPrincipal();
	}
	
	@Override
	public void eraseCredentials() {
		if (this.primary instanceof CredentialsContainer) {
			((CredentialsContainer) this.primary).eraseCredentials();
		}
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	public Authentication getPrimary() {
		return this.primary;
	}

}
