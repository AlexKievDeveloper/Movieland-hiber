package com.hlushkov.movieland.security.util;

import com.hlushkov.movieland.security.jwt.JwtConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final BlockedTokensStoringService blockedTokensStoringService;
    private final JwtConfig jwtConfig;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.replace("Bearer ", "");
            blockedTokensStoringService.addTokenToBlockMap(token);
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
