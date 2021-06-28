package com.hlushkov.movieland.service;

import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieService {

    List<Movie> findAll(FindMoviesRequest findMoviesRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest);
}
