package com.hlushkov.movieland.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "movies")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie implements Cloneable {
    @Id
    @GeneratedValue
    private int id;
    @Column(name = "name_russian")
    private String nameRussian;
    @Column(name = "name_native")
    private String nameNative;
    private String description;
    @Column(name = "year_of_release")
    private int yearOfRelease;
    private double rating;
    private double price;
    @Column(name = "picture_path")
    private String picturePath;
}
