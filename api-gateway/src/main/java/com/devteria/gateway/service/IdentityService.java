package com.devteria.gateway.service;

import org.springframework.stereotype.Service;

import com.devteria.gateway.dto.request.IntrospectRequest;
import com.devteria.gateway.dto.response.ApiResponse;
import com.devteria.gateway.dto.response.IntrospectResponse;
import com.devteria.gateway.repository.IdentityClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class IdentityService {
    private final IdentityClient identityClient;

    public Mono<ApiResponse<IntrospectResponse>> introspect(String accessToken) {
        return identityClient.introspect(IntrospectRequest
                .builder()
                .token(accessToken)
                .build());
    }
}
