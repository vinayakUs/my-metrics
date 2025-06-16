package org.example.accountservice.service;


import org.example.accountservice.domain.Account;

public  interface AccountService {
	
	/**
	 * Finds account by given name
	 *
	 * @param username
	 * @return found account
	 */
	Account findByUserName(String userName);

	
	
}
