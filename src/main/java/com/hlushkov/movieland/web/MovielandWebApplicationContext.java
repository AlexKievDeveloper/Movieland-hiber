package com.hlushkov.movieland.web;

import com.hlushkov.movieland.web.util.SortDirectionRequestParameterConverter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan("com.hlushkov.movieland.web")
public class MovielandWebApplicationContext implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new SortDirectionRequestParameterConverter());
    }
}
