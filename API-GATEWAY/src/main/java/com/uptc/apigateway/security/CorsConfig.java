package com.uptc.apigateway.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    /**
     * Define un filtro de CORS (Cross-Origin Resource Sharing) para manejar
     * solicitudes provenientes de diferentes orígenes
     * @return CorsWebFilter que configura las reglas de CORS.
     */
    @Bean
    public CorsWebFilter corsFilter() {
        // Crear una nueva configuración de CORS.
        CorsConfiguration config = new CorsConfiguration();

        // Permitir solicitudes desde el dominio frontend
//g
        config.addAllowedOriginPattern("http://localhost:3000/");

        // Permitir todos los métodos HTTP (GET, POST, PUT, DELETE).
        config.addAllowedMethod("*");

        // Permitir todos los encabezados HTTP en las solicitudes.
        config.addAllowedHeader("*");

        // Permitir el uso de credenciales, como cookies o tokens de sesión.
        config.setAllowCredentials(true);

        // Configurar la fuente de rutas para aplicar las reglas de CORS.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Aplicar la configuración de CORS a todas las rutas del servidor (`/**`).
        source.registerCorsConfiguration("/**", config);

        // Retornar el filtro de CORS configurado.
        return new CorsWebFilter(source);
    }
}