package org.example.accountservice.service;

import org.example.accountservice.client.AuthServiceClient;
import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.Currency;
import org.example.accountservice.domain.Saving;
import org.example.accountservice.domain.User;
import org.example.accountservice.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	private AccountRepository repo;

	@Autowired
	private AuthServiceClient authClient;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account findByUserName(String userName) {

		Assert.hasText(userName, "Account name must not be null or empty");
		return repo.findByUserName(userName);

	}

	/**
	 * {@inheritDoc}
	 */

	@Override
	public Account create(User user) {

		Account existing = repo.findByUserName(user.getUserName());
		log.info("accoutn found for user: " + existing);
		if(existing != null) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Account already exists username: " + user.getUserName());
		}

		authClient.createUser(user).block();

		Saving saving = new Saving();
		saving.setAmount(new BigDecimal(0));
		saving.setCurrency(Currency.getDefault());
		saving.setInterest(new BigDecimal(0));
		saving.setDeposit(false);
		saving.setCapitalization(false);

		Account account = new Account();
		account.setUsername(user.getUserName());
		account.setLastSeen(new Date());
		account.setSaving(saving);

		repo.save(account);

		log.info("Account created: " + account.getUsername());

		return account;
	}

}
