package org.example.accountservice.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.User;
import org.example.accountservice.dto.ApiResponseDto;
import org.example.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService) {
		this.accountService = accountService;
	}
	

	@GetMapping("/user/{userName}")
	public ResponseEntity<ApiResponseDto<Account>> getAccount(@Valid @NotNull @PathVariable String userName) {
		
		Account acc	= accountService.findByUserName(userName);

		return  ResponseEntity.ok(new ApiResponseDto<Account>(
				true , acc
		));
	}


	@PostMapping("/user")
	public ResponseEntity<ApiResponseDto<Account>> createAccount(@Valid @RequestBody User user) {
        log.info("Creating account received {}", user.getUsername());
		return  ResponseEntity.ok(new ApiResponseDto<Account>(
				true , accountService.create(user)
		));
	}

	@PutMapping("/user")
	public ResponseEntity<ApiResponseDto<Account>> updateAccount(Authentication authentication , @RequestBody Account account) {

		System.out.println(" -----id----- " + authentication.getName());

		Account account_update = accountService.updateAccount(authentication.getName(),account);

		System.out.println(account_update.toString());

		return ResponseEntity.ok(new ApiResponseDto<>(
				true,account_update

		));
	}


}
