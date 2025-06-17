package com.apigateway.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;


@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements GlobalFilter {

    private final JwtService jwtService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return onError(exchange, "Falta el token JWT", HttpStatus.UNAUTHORIZED);
        }

        String token = authHeader.substring(7);

        if (!jwtService.isTokenValid(token)) {
            return onError(exchange, "Token JWT inválido o expirado", HttpStatus.UNAUTHORIZED);
        }

        // Extraer los claims del token
        String username = jwtService.getClaim(token, claims -> claims.get("username", String.class));
        String role = jwtService.getClaim(token, claims -> claims.get("role", String.class));
        String id = jwtService.getClaim(token, claims -> {
            Object idObj = claims.get("id");
            return idObj != null ? idObj.toString() : null;
        });

        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Username", username)
                .header("X-Role", role)
                .header("X-User-Id", id != null ? id : "")
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }


    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bytes);
        return response.writeWith(Mono.just(buffer));
    }
}

