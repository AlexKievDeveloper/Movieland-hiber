package com.hlushkov.movieland.dao;

import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.entity.Movie;

import java.util.List;

public interface MovieDao {

    List<Movie> findAll(FindMoviesRequest findMoviesRequest);

    List<Movie> findRandom();

    List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest);

}
