package org.example.accountservice.client;

import org.example.accountservice.domain.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthServiceClient {

    @Value("${app.properties.webclient.auth-service.host}")
    String authServiceHost;

    private final WebClient webClient;

    public AuthServiceClient(WebClient.Builder clientBuilder) {
        this.webClient  = clientBuilder
                .baseUrl("http://localhost:9000") // ✅ explicitly set the correct port
//                .defaultHeaders(headers -> headers.setBasicAuth("user", "abc"))
                .build();
//        this.webClient = clientBuilder.baseUrl(authServiceHost)
//                .defaultHeaders(headers -> headers.setBasicAuth("user", "abc"))
//                .build();
    }

    public Mono<ResponseEntity<Void>> createUser(User user) {
       return webClient.post().uri("/api/internal/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(user)
                .retrieve()
                .toBodilessEntity()
               .doOnNext(res->System.out.println("createUser in Auth Service: " + res.getStatusCode() ));
    }

}
