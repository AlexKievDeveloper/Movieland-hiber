package com.hlushkov.movieland.security.util;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlockedTokensStoringService {
    private volatile List<String> blockTokensList = new ArrayList<>();

    public void addTokenToBlockList(String token) {
        blockTokensList.add(token);
    }

    public boolean isTokenInBlockList(String token) {
        return blockTokensList.contains(token);
    }
}
