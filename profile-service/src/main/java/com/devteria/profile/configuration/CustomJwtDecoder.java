package com.devteria.profile.configuration;

import java.text.ParseException;
import java.time.Instant;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;

@Component
public class CustomJwtDecoder implements JwtDecoder {
    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            Instant issueTime = signedJWT.getJWTClaimsSet().getIssueTime().toInstant();
            Instant expirationTime =
                    signedJWT.getJWTClaimsSet().getExpirationTime().toInstant();
            return new Jwt(
                    token,
                    issueTime,
                    expirationTime,
                    signedJWT.getHeader().toJSONObject(),
                    signedJWT.getJWTClaimsSet().getClaims());
        } catch (ParseException e) {
            throw new JwtException("Invalid token");
        }
    }
}
