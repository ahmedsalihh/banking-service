package org.salih.banking.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(new org.springframework.security.web.util.matcher.AntPathRequestMatcher("/**"))
                        .authenticated() // Require authentication for all endpoints
                )
                .httpBasic(); // Use HTTP Basic Authentication

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username("admin")
                .password("{noop}admin123") // {noop} indicates plain text password; use bcrypt in production
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }
}
