package org.example.client.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.client.domain.Account;
import org.example.client.dto.ApiResponseDto;
import org.example.client.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.PublicKey;
import java.text.ParseException;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Controller
public class UserController {

    @Autowired
    @Qualifier("default-client-web-client")
    WebClient.Builder webClient;

    @Autowired
    private OAuth2AuthorizedClientService clientService;
    @Autowired
    private OAuth2AuthorizedClientManager clientManager;
    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;


    @PostMapping("/account")
    @ResponseBody
    public ResponseEntity<String> updateAccount(@RequestBody Account account,Authentication authentication) {
        String clientId = "downstream-client"; // from your yml config

        OAuth2AuthorizedClient client =
                authorizedClientService.loadAuthorizedClient(clientId, authentication.getName());
        System.out.println("update account value is " + account);
        System.out.println("client" + client.getAccessToken().getTokenValue());


        OAuth2AccessToken clientAccessToken = client.getAccessToken();

        try {
            Account updatedAccount = this.webClient.build().put()
                    .uri("http://ACCOUNT-SERVICE/accounts/user")
                    .bodyValue(account)
                    .attributes(
                            ServletOAuth2AuthorizedClientExchangeFilterFunction
                            .clientRegistrationId("downstream-client")
                    )
                    .retrieve()
                    .bodyToMono(Account.class)
                    .block();

            System.out.println("updated account value is " + updatedAccount);

        } catch (Exception e) {
            System.out.println("exception message: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update account");
        }




        return ResponseEntity.ok("Done");
    }




//    @PutMapping("/account")
//    @ResponseBody
//    public ResponseEntity<String> updateAccount(@RequestBody Account account
//    ,    @RegisteredOAuth2AuthorizedClient("messaging-client-oidc") OAuth2AuthorizedClient authorizedClient

//    ) {
//
//        System.out.println(
//                "update account value is" + account
//        );
//
//        try{
//            Account updatedAccount = this.webClient.build().put().uri("http://ACCOUNT-SERVICE/accounts/user")
//                    .bodyValue(account)
////                    .headers(h -> h.setBearerAuth(authorizedClient.getAccessToken().getTokenValue()))
//
//                    .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("messaging-client-oidc"))
//                    .retrieve()
//                    .bodyToMono(Account.class)
//                    .block();
//            System.out.println("updated account value is" + updatedAccount  );
//
//        }
//        catch (Exception e){
//            System.out.println("exception message : "  + e.getMessage());
//
//        }
//
//
//        return new ResponseEntity<String>("Done" , HttpStatus.OK);
//    }




    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDto user, RedirectAttributes  model ) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            String response = this.webClient.build().post().uri("http://ACCOUNT-SERVICE/accounts/user")
                    .bodyValue(user)

                    .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("messaging-client-creds-oidc"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ApiResponseDto<?> res= mapper.readValue(response, new TypeReference<ApiResponseDto<?>>(){});



            model.addFlashAttribute("message", "Signup Successful " + res.getData() );
        }
        catch (WebClientResponseException ex){
            String errorBody = ex.getResponseBodyAsString();

            try {
                JsonNode root = mapper.readTree(errorBody);
                JsonNode dataNode = root.get("data");
                if( dataNode != null && dataNode.isObject() ) {
                    // Handle error for Map<?,?>
                    Map<String,String> fieldError = mapper.convertValue(dataNode, new TypeReference<Map<String, String>>() {
                    });
                    StringBuilder stringBuilder=new StringBuilder("Validation Failed: ");

                    fieldError.forEach((k,v)-> stringBuilder.append(k).append(": ").append(v).append(";"));

                    model.addFlashAttribute("message",stringBuilder.toString());


                }else if(dataNode != null && dataNode.isTextual() ) {
                    String message = dataNode.asText();
                    model.addFlashAttribute("message","Signup failed: "+ message);
                }

            }catch (Exception exception){
                model.addFlashAttribute("message", "Signup failed: unknown error format");

            }

        }

        catch (Exception e) {
            model.addAttribute("message", "Signup failed: " + e.getMessage());
        }
        return "redirect:/login";


    }


}
