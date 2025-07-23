package org.example.client.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.client.dto.ApiResponseDto;
import org.example.client.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.text.ParseException;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@Controller
public class UserController {

    @Autowired
    @Qualifier("default-client-web-client")
    WebClient.Builder webClient;

    @Autowired
    private InMemoryClientRegistrationRepository clientRegistrationRepository;



    @PostMapping("/signup")
    public String signup(@ModelAttribute UserDto user, Model model ) {
        ObjectMapper mapper = new ObjectMapper();

        try {

            String response = this.webClient.build().post().uri("http://ACCOUNT-SERVICE/accounts/user")
                    .bodyValue(user)

                    .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId("messaging-client-creds-oidc"))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ApiResponseDto<?> res= mapper.readValue(response, new TypeReference<ApiResponseDto<?>>(){});


            model.addAttribute("message", "Signup Successful " + res.getData());
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

                    model.addAttribute("message",stringBuilder.toString());


                }else if(dataNode != null && dataNode.isTextual() ) {
                    String message = dataNode.asText();
                    model.addAttribute("message","Signup failed: "+ message);
                }

            }catch (Exception exception){
                model.addAttribute("message", "Signup failed: unknown error format");

            }





        }

        catch (Exception e) {

            e.printStackTrace();
            model.addAttribute("message", "Signup failed: " + e.getMessage());

        }
        model.addAttribute("user", new UserDto());
        return "login";


    }


}
