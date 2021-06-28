package com.hlushkov.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "movies")
@Cacheable
@Cache(usage= CacheConcurrencyStrategy.READ_ONLY, region = "movies")
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
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    @JoinTable(
            name = "movies_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;
}
