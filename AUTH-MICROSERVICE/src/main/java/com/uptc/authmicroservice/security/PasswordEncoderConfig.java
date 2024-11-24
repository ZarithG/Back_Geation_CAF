package com.uptc.authmicroservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuración para el codificador de contraseñas.
 * Este componente proporciona una instancia de `PasswordEncoder` para cifrar contraseñas
 * y validar contraseñas cifradas en el sistema.
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * Declara un bean que retorna un codificador de contraseñas basado en BCrypt.
     * BCrypt es un algoritmo de hashing diseñado específicamente para proteger contraseñas,
     * con un enfoque en la seguridad frente a ataques de fuerza bruta.
     *
     * @return Una instancia de `BCryptPasswordEncoder`.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

