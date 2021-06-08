package com.hlushkov.movieland.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan("com.hlushkov.movieland.web")
public class MovielandWebApplicationContext implements WebMvcConfigurer {


}
