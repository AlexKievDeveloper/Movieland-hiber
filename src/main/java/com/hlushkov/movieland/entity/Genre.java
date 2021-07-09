package com.hlushkov.movieland.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "genres")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "genres")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"movies"})
@Builder
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @JsonIgnore
    @ManyToMany(mappedBy = "genres")
    private Set<Movie> movies;
}
