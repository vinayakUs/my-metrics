package org.example.accountservice.repository;

import org.example.accountservice.domain.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AccountRepository extends CrudRepository<Account, String> {
	
	Account findByUserName(String userName);

}
