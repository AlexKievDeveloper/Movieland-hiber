package com.hlushkov.movieland.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "genres")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Genre {
    @Id
    @GeneratedValue
    private int id;
    private String name;
}
