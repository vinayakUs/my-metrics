package org.example.accountservice.service;


import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.User;

public  interface AccountService {
	
	/**
	 * Finds account by given name
	 *
	 * @param username
	 * @return found account
	 */
	Account findByUserName(String userName);


	/*
	 * create a user account on post
	 * @param User
	 * @return null
	 */
	Account create(User user);
	
	
}
