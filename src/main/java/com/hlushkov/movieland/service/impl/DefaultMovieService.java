package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.jpa.MovieRepository;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieRepository movieRepository;

    @Override
    public List<Movie> findAll() {
        return movieRepository.findAll();
    }

    @Override
    public List<Movie> findRandom() {
        return movieRepository.findRandom();
    }

    @Override
    public List<Movie> findByGenre(int genreId) {
        return movieRepository.findByGenre(genreId);
    }
}
