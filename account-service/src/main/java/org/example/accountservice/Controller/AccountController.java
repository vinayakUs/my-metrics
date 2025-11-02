package org.example.accountservice.Controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.accountservice.client.StatisticsServiceClient;
import org.example.accountservice.domain.Account;
import org.example.accountservice.domain.User;
import org.example.accountservice.dto.ApiResponseDto;
import org.example.accountservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;


@RestController
@Slf4j
@RequestMapping("/accounts")
public class AccountController {

	private final AccountService accountService;

	public AccountController(AccountService accountService, StatisticsServiceClient statisticsClient) {
		this.accountService = accountService;
        this.statisticsClient = statisticsClient;
    }
	

	@GetMapping("/user/{userName}")
	public ResponseEntity<ApiResponseDto<Account>> getAccount(@Valid @NotNull @PathVariable String userName) {
		
		Account acc	= accountService.findByUserName(userName);
        System.out.println(acc.getIncomes().get(0).toString());
		return  ResponseEntity.ok(new ApiResponseDto<Account>(
				true , acc
		));
	}
    private final StatisticsServiceClient statisticsClient;


	@PostMapping("/user")
	public ResponseEntity<ApiResponseDto<Account>> createAccount(@Valid @RequestBody User user) {
        log.info("Creating account received {}", user.getUsername());
		return  ResponseEntity.ok(new ApiResponseDto<Account>(
				true , accountService.create(user)
		));
	}

	@PutMapping("/user")
	public ResponseEntity<ApiResponseDto<Account>> updateAccount(Authentication authentication , @RequestBody Account account, @AuthenticationPrincipal OAuth2User principal,

                                                                 @RegisteredOAuth2AuthorizedClient("account-service-as-stat-service-token-exchange")
                                                                 OAuth2AuthorizedClient authorizedClient
                                                                 ) {

		System.out.println(" -----id----- " + authentication.getName() + authorizedClient.getAccessToken().getTokenValue() );

		Account account_update = accountService.updateAccount(authentication.getName(),account);


        statisticsClient.postStatistic(account_update,authorizedClient.getAccessToken().getTokenValue())
                .doOnSuccess(response -> {log.info("Statistic posted successfully");})
                .doOnError(error -> {
                    log.error("Failed to post statistic{}", error.getMessage());})
                .subscribe();

		return ResponseEntity.ok(new ApiResponseDto<>(
				true,account_update
		));
	}


}
