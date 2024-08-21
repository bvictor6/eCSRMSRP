package org.bcms.ecsrmsrp.mfa.account;

import java.util.Collection;

import org.bcms.ecsrmsrp.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class AccountUserDetails implements UserDetails {

	private static final long serialVersionUID = -2030384536172924069L;
	private final User account;

	public AccountUserDetails(User account) {
		this.account = account;
	}

	public User getAccount() {
		return account;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return AuthorityUtils.createAuthorityList("ROLE_USER");
	}

	@Override
	public String getPassword() {
		return this.account.getPassword();
	}

	@Override
	public String getUsername() {
		return this.account.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String toString() {
		return "AccountUserDetails{" + "account=" + account + '}';
	}

}
