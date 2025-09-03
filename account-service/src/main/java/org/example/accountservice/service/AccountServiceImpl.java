package org.example.accountservice.service;

import org.example.accountservice.client.AuthServiceClient;
import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.Currency;
import org.example.accountservice.domain.Saving;
import org.example.accountservice.domain.User;
import org.example.accountservice.exceptions.ResourceAlreadyExist;
import org.example.accountservice.exceptions.ResourceNotFound;
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
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository repo;

	private final AuthServiceClient authClient;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Account findByUserName(String userName) {
		Optional<Account> acc = repo.findByUserName(userName);
		if(acc.isPresent()) {
			return repo.findByUserName(userName).get();
		}
		throw new ResourceNotFound("No user Exist for username: " + userName);
    }

	/**
	 * {@inheritDoc}
	 */

	@Override
	public Account create(User user) {

		Optional<Account> existing = repo.findByUserName(user.getUsername());

		if (existing.isPresent()) {
			throw new ResourceAlreadyExist( "Account already exists username: " + user.getUsername());
		}


		authClient.createUser(user).block();

		Saving saving = new Saving();
		saving.setAmount(new BigDecimal(0));
		saving.setCurrency(Currency.getDefault());
		saving.setInterest(new BigDecimal(0));
		saving.setDeposit(false);
		saving.setCapitalization(false);

		Account account = new Account();

		account.setUsername(user.getUsername());
		account.setLastSeen(new Date());
		account.setSaving(saving);

		repo.save(account);

        log.info("Account created: {}", account.getUsername());

		return account;
	}

	/**
	 *{@inheritDoc}
	 */
	@Override
	public Account updateAccount(String userName, Account account) {

		Optional<Account> existingO = repo.findByUserName(userName);
		if( existingO.isPresent()) {
			Account existing = existingO.get();
			existing.setLastSeen(account.getLastSeen());
			existing.setSaving(account.getSaving());
			existing.setIncomes(account.getIncomes());
			existing.setExpenses(account.getExpenses());
			repo.save(existing);
			return existing;
		}
		throw new ResourceNotFound("No user Details exist for username: " + userName);

	}

}
