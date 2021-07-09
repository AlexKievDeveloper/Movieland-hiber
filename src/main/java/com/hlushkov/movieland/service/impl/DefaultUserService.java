package com.hlushkov.movieland.service.impl;

import com.hlushkov.movieland.common.request.SignUpRequest;
import com.hlushkov.movieland.dao.UserDao;
import com.hlushkov.movieland.entity.MovielandUser;
import com.hlushkov.movieland.entity.MovielandUserRole;
import com.hlushkov.movieland.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultUserService implements UserDetailsService, UserService {

    private final UserDao userDao;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MovielandUser> optionalUser = userDao.findByUsername(username);
        if (optionalUser.isPresent()) {
            MovielandUser movielandUser = optionalUser.get();

            List<GrantedAuthority> grantedAuthorities = getGrantedAuthorities(movielandUser);

            return new User(movielandUser.getUsername(),
                    movielandUser.getPassword(), movielandUser.isEnabled(),
                    true, true, true, grantedAuthorities);

        } else throw new UsernameNotFoundException(String.format("username %s not found", username));
    }

    private List<GrantedAuthority> getGrantedAuthorities(MovielandUser movielandUser) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(movielandUser.getMovielandUserRole().name())));
        return grantedAuthorities;
    }

    public void signUp(SignUpRequest signUpRequest) {
       MovielandUser movielandUser = MovielandUser.builder()
                .username(signUpRequest.getUsername())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .email(signUpRequest.getEmail())
                .movielandUserRole(MovielandUserRole.USER)
                .isEnabled(true).build();
        userDao.save(movielandUser);
    }
}
