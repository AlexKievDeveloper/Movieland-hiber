package com.hlushkov.movieland.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import net.jodah.expiringmap.ExpiringMap;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlockedTokensStoringService {
    private final SecretKey secretKey;

    private final ExpiringMap<String, Claims> tokenExpiringMap = ExpiringMap.builder()
            .variableExpiration()
            .maxSize(1000)
            .build();

    public void addTokenToBlockMap(String token) {
        Jws<Claims> claimsJws = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);

        Claims claimsBody = claimsJws.getBody();
        tokenExpiringMap.put(token, claimsBody, getTTLForToken(claimsBody.getExpiration()), TimeUnit.SECONDS);
    }

    public boolean isTokenInBlockList(String token) {
        return tokenExpiringMap.containsKey(token);
    }

    private long getTTLForToken(Date date) {
        long timeWhenExpiry = date.toInstant().getEpochSecond();
        long timeNow = Instant.now().getEpochSecond();
        return Math.max(0, timeWhenExpiry - timeNow);
    }
}
