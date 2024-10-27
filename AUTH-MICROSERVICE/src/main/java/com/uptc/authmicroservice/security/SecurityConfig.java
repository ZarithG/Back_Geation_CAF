package com.uptc.authmicroservice.security;

import com.uptc.authmicroservice.succeshandler.CustomOAuth2SuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{
    @Autowired
    CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/auth/create","/auth/validate","/auth/google").permitAll()
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customOAuth2SuccessHandler));
        return http.build();
    }

// ---------------------------------------------------

//    @Bean
//    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/auth/login", "/auth/create").permitAll()
//                                .anyRequest().authenticated())
//                .oauth2Login(oauth2 ->
//                        oauth2.userInfoEndpoint(userInfo ->
//                                userInfo.userService(customOAuth2UserService)
//                        ).defaultSuccessUrl("/auth/welcome", true)
//                );
//
////                .formLogin(Customizer.withDefaults());
////                .formLogin(form -> form.defaultSuccessUrl("/auth/welcome", true));
//        return http.build();
//    }

//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorizeRequests ->
//                        authorizeRequests
//                                .requestMatchers("/auth/login", "/auth/create", "/auth/validate").permitAll() // Permite acceso a estos endpoints
//                                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
//                )
//                .oauth2Login(oauth2 ->
//                        oauth2.defaultSuccessUrl("/auth/welcome", true)
//                );
//
//        return http.build();
//    }
}
