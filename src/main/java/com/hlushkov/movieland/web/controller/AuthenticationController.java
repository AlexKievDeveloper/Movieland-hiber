package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.request.SignUpRequest;
import com.hlushkov.movieland.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthenticationController {
    private final UserService defaultUserService;

    @GetMapping("/")
    public void home(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @PostMapping("signUp")
    public void signUp(@RequestBody SignUpRequest signUpRequest, HttpServletResponse response) {
        defaultUserService.signUp(signUpRequest);
        response.setStatus(HttpStatus.CREATED.value());
    }
}
