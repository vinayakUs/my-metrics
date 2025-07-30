package org.example.client.controller;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.client.domain.Account;
import org.example.client.domain.Currency;
import org.example.client.domain.Item;
import org.example.client.domain.TimePeriod;
import org.example.client.dto.ApiResponseDto;
import org.example.client.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    @Qualifier("default-client-web-client")
    WebClient.Builder webClient;


    @GetMapping("/login")
    public String login(Authentication authentication,Model model) {
        model.addAttribute("user", new UserDto());
        if(authentication != null &&  authentication.isAuthenticated()){
            return "redirect:/";
        }
        return "login";
    }

 
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("username", principal.getAttributes().get("sub"));







        return "index";
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal OAuth2User principal) {
        model.addAttribute("username", principal.getAttributes().get("sub"));


        model.addAttribute("message", "Welcome back!");
        model.addAttribute("toastType", "text-bg-success"); // green toast

        ObjectMapper objectMapper = new ObjectMapper();

        try{
            System.out.println(principal.getAttributes().get("sub"));
            String response = webClient.build().get().uri("http://ACCOUNT-SERVICE/accounts/user/"+principal.getAttributes().get("sub"))
                    .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("messaging-client-creds-oidc"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            System.out.println(response);
            Account account = objectMapper.readValue(response, new TypeReference<ApiResponseDto<Account>>() {
            }).getData();

            System.out.println("fetch account is: " + account);

            model.addAttribute("account", account);



        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


        return "dashboard";
    }




}
