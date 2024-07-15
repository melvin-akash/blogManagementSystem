package com.melvin.blogManagementSystem.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests((requests) -> {
            ((AuthorizeHttpRequestsConfigurer.AuthorizedUrl)requests
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest()).authenticated();
        });
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(withDefaults());
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));
        return (SecurityFilterChain)http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails USER1 = User.withUsername("user1")
                .password(passwordEncoder().encode("pass"))
                .roles("USER")
                .build();
        UserDetails USER2 = User.withUsername("user2")
                .password(passwordEncoder().encode("pass"))
                .roles("USER")
                .build();
        UserDetails USER3 = User.withUsername("user3")
                .password(passwordEncoder().encode("pass"))
                .roles("ADMIN")
                .build();

        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        jdbcUserDetailsManager.createUser(USER1);
        jdbcUserDetailsManager.createUser(USER2);
        jdbcUserDetailsManager.createUser(USER3);

        return jdbcUserDetailsManager;
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
