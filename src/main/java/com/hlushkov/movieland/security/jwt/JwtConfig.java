package com.hlushkov.movieland.security.jwt;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Data
@NoArgsConstructor
public class JwtConfig {
    @Value("${application.jwt.secretKey}")
    private String secretKey;
    @Value("${application.jwt.tokenPrefix}")
    private String tokenPrefix;
    @Value("${application.jwt.tokenExpirationAfterSec}")
    private Integer tokenExpirationAfterSec;
    @Value("${application.jwt.refreshTokenExpirationSec}")
    private Integer refreshTokenExpirationAfterSec;

    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
