package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.dao.jpa.GenreRepository;
import com.hlushkov.movieland.entity.Genre;
import com.hlushkov.movieland.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultGenreService implements GenreService {
    private final GenreRepository genreRepository;

    @Override
    public List<Genre> findAll() {
        return genreRepository.findAll();
    }
}
