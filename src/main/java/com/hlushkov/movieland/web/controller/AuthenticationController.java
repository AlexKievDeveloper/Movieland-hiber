package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.request.SignUpRequest;
import com.hlushkov.movieland.security.jwt.JwtConfig;
import com.hlushkov.movieland.security.util.BlockedTokensStoringService;
import com.hlushkov.movieland.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService defaultUserService;
    private final BlockedTokensStoringService blockedTokensStoringService;
    private final JwtConfig jwtConfig;

    @PostMapping("signUp")
    public void signUp(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        defaultUserService.signUp(signUpRequest);
        response.setStatus(HttpStatus.CREATED.value());
    }

    @GetMapping("logout")
    public void logout(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());
        String token = authorizationHeader.replace("Bearer ", "");
        blockedTokensStoringService.addTokenToBlockList(token);
    }

}
