package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.jpa.HibernateMovieDao;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultMovieService implements MovieService {
    @Autowired
    private HibernateMovieDao hibernateMovieDao;

    @Override
    public List<Movie> findAll() {
        return hibernateMovieDao.findAll();
    }
}
