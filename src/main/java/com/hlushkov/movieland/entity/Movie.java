package com.hlushkov.movieland.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@Builder
public class Movie implements Cloneable {
    int id;
    String nameRussian;
    String nameNative;
    String description;
    int yearOfRelease;
    double rating;
    double price;
    String picturePath;
}
