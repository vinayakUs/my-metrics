package org.example.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class OAuth2LoginConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login","/signup","/favicon.ico").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(
                        oauth2Login -> oauth2Login.loginPage("/login")
                )
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login").invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"))
                .oauth2Client(withDefaults());
        return http.build();
    }

}
