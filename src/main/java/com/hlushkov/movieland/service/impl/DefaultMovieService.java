package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.dao.MovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DefaultMovieService implements MovieService {
    private final MovieDao movieDao;

    @Override
    public List<Movie> findAll(FindMoviesRequest findMoviesRequest) {
        return movieDao.findAll(findMoviesRequest);
    }

    @Override
    public List<Movie> findRandom() {
        return movieDao.findRandom();
    }

    @Override
    public List<Movie> findByGenre(int genreId, FindMoviesRequest findMoviesRequest) {
        return movieDao.findByGenre(genreId, findMoviesRequest);
    }
}
