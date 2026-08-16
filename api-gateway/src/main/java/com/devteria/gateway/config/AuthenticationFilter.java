package com.devteria.gateway.config;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.service.IdentityService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {

    private final IdentityService identityService;
    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        List<String> authHeaders = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeaders)) {
            return unauthenticated(exchange.getResponse());
        }
        String accessToken = authHeaders.getFirst().substring(7);
        return identityService.introspect(accessToken).flatMap(res -> {
            if (res.getResult().isValid()) {
                return chain.filter(exchange);
            } else {
                return unauthenticated(exchange.getResponse());
            }
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        ApiResponse<String> apiRes = ApiResponse.<String>builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message("Unauthenticated")
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(apiRes);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response
                .writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }

}
