package com.hlushkov.movieland.common.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignUpRequest {
    private String username;
    private String password;
    private String email;
}
