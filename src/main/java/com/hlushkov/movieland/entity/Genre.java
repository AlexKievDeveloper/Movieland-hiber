package com.hlushkov.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "genres")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"movies"})
@Builder
public class Genre {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;
}
