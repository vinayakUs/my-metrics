package org.example.accountservice.Controller;

import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.User;
import org.example.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/accounts")
public class AccountController {
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/user/{userName}")
	public ResponseEntity<Account> getAccount(@PathVariable String userName) {
		
		Account acc	= accountService.findByUserName(userName);
		if(acc==null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(acc);
		
	}


	@PostMapping("/user")
	public ResponseEntity<Account> createAccount(@RequestBody User user) {
		return  ResponseEntity.ok(accountService.create(user));
	}

}
