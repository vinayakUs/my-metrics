package org.example.statisticsservice;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration(proxyBeanMethods = false)
//@EnableWebSecurity
//public class ResourceServerConfig {
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
//        http.
//                authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/public/**","/actuator/**","/test/**").permitAll() // allow public access
//                        .anyRequest().authenticated()              // secure all other endpoints
//                )
//                .oauth2ResourceServer(oauth2ResourceServer ->
//                        oauth2ResourceServer.jwt(   Customizer.withDefaults())
//                );
//        return http.build();
//
//    }
//
//
//}