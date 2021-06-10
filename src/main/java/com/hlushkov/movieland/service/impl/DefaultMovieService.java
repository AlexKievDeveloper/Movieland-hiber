package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultMovieService implements MovieService {

    @Override
    public List<Movie> findAll() {
        return null;
    }
}
