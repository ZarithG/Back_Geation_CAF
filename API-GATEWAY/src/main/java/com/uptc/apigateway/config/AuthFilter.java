package com.uptc.apigateway.config;

import com.uptc.apigateway.dto.RequestDTO;
import com.uptc.apigateway.dto.TokenDTO;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Filtro de autenticación personalizado para verificar la validez de un token JWT
 * y asegurar que el usuario tiene acceso permitido a la URL solicitada en función
 * de su rol. Este filtro intercepta las solicitudes y valida los encabezados de autorización.
 */
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    // Builder para realizar peticiones HTTP usando WebClient.
    private WebClient.Builder webClient;

    /**
     * Constructor del filtro de autenticación.
     *
     * @param webClient WebClient.Builder para enviar solicitudes HTTP al servicio de autenticación.
     */
    public AuthFilter(WebClient.Builder webClient) {
        super(Config.class);
        this.webClient = webClient;
    }

    /**
     * Método principal del filtro que verifica el token JWT y la autorización del usuario.
     *
     * @param config Configuración del filtro (sin uso en este caso).
     * @return Un filtro de Gateway que realiza la validación del token y la autorización.
     */
    @Override
    public GatewayFilter apply(Config config) {
        return (((exchange, chain) -> {
            // Verificar si la solicitud contiene el encabezado de autorización.
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange, HttpStatus.BAD_REQUEST);

            // Extraer el token del encabezado de autorización.
            String tokenHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] chunks = tokenHeader.split(" ");

            // Validar el formato del token ("Bearer <token>").
            if (chunks.length != 2 || !chunks[0].equals("Bearer"))
                return onError(exchange, HttpStatus.BAD_REQUEST);

            // Enviar solicitud al microservicio de autenticación para validar el token.
            return webClient.build()
                    .post()
                    .uri("http://AUTH-MICROSERVICE/auth/validate?token=" + chunks[1]) // Validar el token.
                    .bodyValue(new RequestDTO(
                            exchange.getRequest().getPath().toString(), // Ruta solicitada.
                            exchange.getRequest().getMethod().toString() // Método HTTP.
                    ))
                    .retrieve() // Realizar la solicitud y recuperar la respuesta.
                    .bodyToMono(TokenDTO.class) // Convertir la respuesta en un TokenDTO.
                    .map(t -> {
                        // Validación exitosa: Continuar con la cadena de filtros.
                        t.getToken(); // Aquí se podría realizar más procesamiento si es necesario.
                        return exchange;
                    }).flatMap(chain::filter); // Continuar con el siguiente filtro.
        }));
    }

    /**
     * Maneja los errores del filtro devolviendo un estado HTTP apropiado al cliente.
     *
     * @param exchange El intercambio de servidor web que contiene la solicitud y la respuesta.
     * @param status   El estado HTTP a devolver.
     * @return Un `Mono<Void>` que completa la respuesta con el código de estado especificado.
     */
    public Mono<Void> onError(ServerWebExchange exchange, HttpStatus status) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(status); // Establecer el código de estado HTTP.
        return response.setComplete(); // Completar la respuesta sin contenido adicional.
    }

    /**
     * Clase estática para configurar el filtro. Actualmente no se utiliza pero
     * se requiere para cumplir con la API de AbstractGatewayFilterFactory.
     */
    public static class Config {}
}

