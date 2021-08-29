package com.kangmin.app.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class WebUserDetailsService implements UserDetailsService {

    private final WebUserDetailsDao webUserDetailsDao;

    @Autowired
    public WebUserDetailsService(
        @Qualifier("UserDetailsDaoJpaImpl") final WebUserDetailsDao webUserDetailsDao
    ) {
        this.webUserDetailsDao = webUserDetailsDao;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return webUserDetailsDao
                .getUserDetailsByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(String.format("Username %s not found", username))
                );
    }
}
