package org.example.accountservice.service;

import org.example.accountservice.domain.Account;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository repo;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account findByUserName(String userName) {

		Assert.hasText(userName, "Account name must not be null or empty");
		return repo.findByUserName(userName);

	}

}
