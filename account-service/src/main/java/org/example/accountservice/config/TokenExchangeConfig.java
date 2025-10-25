package org.example.accountservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.util.Assert;

import java.util.function.Function;

@Configuration
public class TokenExchangeConfig {
    private static final String ACTOR_TOKEN_CLIENT_REGISTRATION_ID = "account-service-client-credentials";




    @Bean
    public OAuth2AuthorizedClientProvider  tokenOauth2AuthorizedClientProvider(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService
    ) {
        OAuth2AuthorizedClientManager authorizedClientManager =
                tokenExchangeAuthorizedClientManager(clientRegistrationRepository, authorizedClientService);

        Function<OAuth2AuthorizationContext, OAuth2Token> actorTokenResolver = createTokenResolver(
                authorizedClientManager, ACTOR_TOKEN_CLIENT_REGISTRATION_ID);

        TokenExchangeOAuth2AuthorizedClientProvider tokenExchangeAuthorizedClientProvider =
                new TokenExchangeOAuth2AuthorizedClientProvider();
        tokenExchangeAuthorizedClientProvider.setActorTokenResolver(actorTokenResolver);
        return tokenExchangeAuthorizedClientProvider;

    }

    /**
     * Create a {@code Function} to resolve a token from the current principal.
     */
    private static Function<OAuth2AuthorizationContext, OAuth2Token> createTokenResolver(
            OAuth2AuthorizedClientManager authorizedClientManager, String clientRegistrationId) {

        return (context)->
        {
//            Do not provide an actor token for impersonation use case - add if using impersonation token

            // @formatter:off
            OAuth2AuthorizeRequest authorizeRequest =
                    OAuth2AuthorizeRequest.withClientRegistrationId(clientRegistrationId)
                            .principal(context.getPrincipal())
                            .build();
            // @formatter:on

            OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);
            Assert.notNull(authorizedClient, "authorizedClient cannot be null");

            return authorizedClient.getAccessToken();

        };

    }

    private OAuth2AuthorizedClientManager tokenExchangeAuthorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientService authorizedClientService) {
        OAuth2AuthorizedClientProvider authorizedClientProvider =
                OAuth2AuthorizedClientProviderBuilder.builder()
                        .clientCredentials()
                        .build();
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);

        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;

    }

//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientService authorizedClientService,
//            OAuth2AuthorizedClientProvider tokenExchangeProvider // <-- your custom provider
//    ) {
//        // This is the manager Spring will inject everywhere (e.g., WebClient)
//        AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
//                new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
//
//        // Tell it to use your Token Exchange provider (which internally uses the helper manager)
//        manager.setAuthorizedClientProvider(tokenExchangeProvider);
//
//        return manager;
//    }



}
