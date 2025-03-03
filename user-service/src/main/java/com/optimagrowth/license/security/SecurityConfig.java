package com.optimagrowth.license.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // You can configure it further if needed
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Disable CSRF protection for testing
            .authorizeRequests()
                .antMatchers("/**").permitAll()  // Allow access to all endpoints
            .anyRequest().permitAll()  // Allow access to any request without authentication
            .and()
            .formLogin().disable() // Disable form login
            .httpBasic().disable(); // Disable HTTP Basic authentication
    }
}

