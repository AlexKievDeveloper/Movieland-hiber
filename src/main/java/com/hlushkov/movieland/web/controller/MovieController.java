package com.hlushkov.movieland.web.controller;

import com.hlushkov.movieland.common.SortDirection;
import com.hlushkov.movieland.common.request.FindMoviesRequest;
import com.hlushkov.movieland.entity.Movie;
import com.hlushkov.movieland.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "movie", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RestController
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> findAll(@RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                               @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {
        log.debug("Request for all movies received");
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findAll(findMoviesRequest);
    }

    @GetMapping(value = "random")
    public List<Movie> findRandom() {
        return movieService.findRandom();
    }

    @GetMapping(value = "genre/{genreId}")
    public List<Movie> findByGenre(@PathVariable int genreId,
                                   @RequestParam(value = "rating", required = false) SortDirection ratingSortDirection,
                                   @RequestParam(value = "price", required = false) SortDirection priceSortDirection) {
        log.debug("Request for movies by genre received");
        FindMoviesRequest findMoviesRequest = new FindMoviesRequest();
        findMoviesRequest.setRatingDirection(Optional.ofNullable(ratingSortDirection));
        findMoviesRequest.setPriceDirection(Optional.ofNullable(priceSortDirection));
        return movieService.findByGenre(genreId, findMoviesRequest);
    }
}
