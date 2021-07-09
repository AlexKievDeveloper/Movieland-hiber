package com.hlushkov.movieland.entity;

import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class MovielandUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String username;
    private String password;
    private String email;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private MovielandUserRole movielandUserRole;
    @Column(name = "is_enabled")
    private boolean isEnabled;
}
