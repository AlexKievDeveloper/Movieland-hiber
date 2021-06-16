package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "movie", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findAll() {
        return movieService.findAll();
    }

    @GetMapping(value = "random")
    public List<Movie> findRandom() {
        return movieService.findRandom();
    }

    @GetMapping(value = "genre/{genreId}")
    public List<Movie> findByGenre(@PathVariable int genreId) {
        return movieService.findByGenre(genreId);
    }
}
