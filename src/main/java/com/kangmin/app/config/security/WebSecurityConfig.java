package com.kangmin.app.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.kangmin.app.model.security.WebUserPermission.ACCOUNT_READ;
import static com.kangmin.app.model.security.WebUserRole.ADMIN;
import static com.kangmin.app.model.security.WebUserRole.SUPER_ADMIN;

@Import({
    PasswordConfig.class,
})
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService webUserDetailsService;

    public WebSecurityConfig(
        final PasswordEncoder passwordEncoder,
        final UserDetailsService webUserDetailsService
    ) {
        this.passwordEncoder = passwordEncoder;
        this.webUserDetailsService = webUserDetailsService;
    }

    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf()
            .disable()
            .headers()
            .frameOptions().disable()
            .and()
            .authorizeRequests()
            .antMatchers("/",
                "/manifest.json",
                "/favicon.ico",
                "logo*.png",
                "/static/**",
                "/css/**",
                "/js/**",
                "/lib/**",
                "/img/**",
                "/webjars/**",
                "/resources/**",
                "/error/**",
                "/index",
                "/home",
                "/blogs/**",
                "/category/**",
                "/test/**",
                "/category",
                "/about",
                "/login", "/auth/login", "/api/auth/login",
                "/register", "/auth/register", "/api/auth/register",
                "/h2-console*",
                "/h2-console/*"
            )
            .permitAll()
            .antMatchers("/api/accounts/**")
            .hasAnyAuthority(ACCOUNT_READ.getName())
            .antMatchers("/admin/**")
            .hasAnyRole(ADMIN.name(), SUPER_ADMIN.name())
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/authenticateTheUser")
            .defaultSuccessUrl("/blogs")
            .and()
            .logout().permitAll()
            .logoutSuccessUrl("/login?logout=true")
            .deleteCookies("JSESSIONID")
            .invalidateHttpSession(true);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(webUserDetailsService);
        return provider;
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
