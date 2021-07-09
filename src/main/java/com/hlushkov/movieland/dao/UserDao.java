package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.entity.MovielandUser;

import java.util.Optional;

public interface UserDao {

    Optional<MovielandUser> findByUsername(String username);

    void save(MovielandUser movielandUser);
}
