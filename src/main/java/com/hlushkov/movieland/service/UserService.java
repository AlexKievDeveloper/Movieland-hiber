package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.request.SignUpRequest;

public interface UserService {

    void signUp(SignUpRequest signUpRequest);
}
