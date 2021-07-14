package com.hlushkov.movieland.security.config;

import com.hlushkov.movieland.security.jwt.JwtConfig;
import com.hlushkov.movieland.security.jwt.JwtTokenVerifier;
import com.hlushkov.movieland.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.hlushkov.movieland.security.util.BlockedTokensStoringService;
import com.hlushkov.movieland.security.util.LogoutService;
import com.hlushkov.movieland.service.impl.DefaultUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.crypto.SecretKey;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final DefaultUserService defaultUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final BlockedTokensStoringService blockedTokensStoringService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        RequestMatcher loginRequestMatcher = new AntPathRequestMatcher("/api/v1/login", "POST");
        RequestMatcher logoutRequestMatcher = new AntPathRequestMatcher("/api/v1/logout", "GET");

        JwtUsernameAndPasswordAuthenticationFilter jwtUsernameAndPasswordAuthenticationFilter
                = new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, secretKey);
        jwtUsernameAndPasswordAuthenticationFilter.setRequiresAuthenticationRequestMatcher(loginRequestMatcher);

        http
                .csrf()
                .ignoringRequestMatchers(loginRequestMatcher, logoutRequestMatcher)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(jwtUsernameAndPasswordAuthenticationFilter)
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig, blockedTokensStoringService),
                        JwtUsernameAndPasswordAuthenticationFilter.class)
                .logout()
                .logoutUrl("/api/v1/logout")
                .logoutSuccessUrl("/api/v1/")
                .logoutRequestMatcher(logoutRequestMatcher)
                .addLogoutHandler(new LogoutService(blockedTokensStoringService, jwtConfig))
                .and()
                .authorizeRequests()
                .antMatchers("/api/v1/", "/api/v1/signUp").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(defaultUserService);
        return daoAuthenticationProvider;
    }

}
