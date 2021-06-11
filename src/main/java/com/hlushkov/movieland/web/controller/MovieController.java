package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("movie")
@RestController
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findAll() {
        return movieService.findAll();
    }

}
