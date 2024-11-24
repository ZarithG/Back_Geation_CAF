package com.uptc.authmicroservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración para proporcionar un bean de RestTemplate con balanceo de carga habilitado.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * Crea una instancia de {@link RestTemplate} con balanceo de carga habilitado.
     * El balanceo de carga es proporcionado por Spring Cloud, lo que permite que las
     * solicitudes se distribuyan entre instancias de servicios registrados en un servidor
     * de descubrimiento como Eureka.
     *
     * @return una instancia configurada de {@link RestTemplate}.
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

