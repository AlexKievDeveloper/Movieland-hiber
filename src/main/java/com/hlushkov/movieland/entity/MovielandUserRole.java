package com.hlushkov.movieland.entity;

public enum MovielandUserRole {
    ADMIN("ADMIN"), USER("USER");

    private final String userRoleName;

    MovielandUserRole(String userRoleName) {
        this.userRoleName = userRoleName;
    }

    public String getUserRoleName() {
        return userRoleName;
    }

    @Override
    public String toString() {
        return "MovielandUserRole{" +
                "userRoleName='" + userRoleName + '\'' +
                '}';
    }
}
