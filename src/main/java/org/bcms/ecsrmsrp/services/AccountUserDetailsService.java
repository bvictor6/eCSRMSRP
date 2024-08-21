package org.bcms.ecsrmsrp.services;

import org.bcms.ecsrmsrp.entities.User;
import org.bcms.ecsrmsrp.mfa.account.Account;
import org.bcms.ecsrmsrp.mfa.account.AccountUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AccountUserDetailsService implements UserDetailsService {

	@Autowired UserService userService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		try {
			User user = userService.findByUsername(username);
			Account account  = new Account(user.getUsername(), user.getPassword(), user.getTwoFactorSecret(), false);
			return new AccountUserDetails(account);
		}
		catch (EmptyResultDataAccessException e) {
			throw new UsernameNotFoundException("user not found", e);
		}
	}

}
