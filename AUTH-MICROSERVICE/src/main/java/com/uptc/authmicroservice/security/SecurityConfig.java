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

import java.util.ArrayList;

/**
 * Configuración de seguridad de la aplicación utilizando Spring Security.
 * Define las rutas públicas y el manejo de autenticación mediante OAuth2.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Componente personalizado para manejar el éxito en la autenticación OAuth2.
     */
    @Autowired
    CustomOAuth2SuccessHandler customOAuth2SuccessHandler;

    /**
     * Rutas públicas que no requieren autenticación.
     * Estas rutas están explícitamente permitidas para accesos anónimos.
     */
    private static final String[] PUBLIC_ROUTES = {
            "/auth/login", "/auth/create", "/auth/validate", "/auth/google",
            "/auth/change/password", "/auth/change-role",
            "/auth/verify/user", "/auth/google/login",
            "/auth/change-wellbeing-director", "/auth/active-user/**",
            "/auth/inactive-user/**", "/auth/isUserVerified/**",
            "/auth/user/all", "/auth/user/caf/all",
            "/auth/user/is-registered/**"
    };

    /**
     * Configura la cadena de filtros de seguridad para manejar solicitudes HTTP.
     *
     * @param http El objeto `HttpSecurity` utilizado para configurar la seguridad.
     * @return Una instancia de `SecurityFilterChain`.
     * @throws Exception Si ocurre algún error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configuración de la seguridad HTTP
        http.csrf(AbstractHttpConfigurer::disable) // Desactiva la protección CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ROUTES).permitAll() // Permite las rutas públicas
                        .anyRequest().authenticated()) // Requiere autenticación para todas las demás rutas
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(customOAuth2SuccessHandler)); // Configura el manejo de éxito de OAuth2
        return http.build(); // Construye la cadena de filtros de seguridad
    }
}

