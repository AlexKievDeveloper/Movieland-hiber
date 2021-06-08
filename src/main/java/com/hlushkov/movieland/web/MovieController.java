package com.hlushkov.movieland.web;

import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/movie")
@RestController
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findAll() {
        return movieService.findAll();
    }

}
