package it.unimol.newunimol;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("password"))
                .roles("ADMIN")
                .build();

        UserDetails teacher = User.withUsername("teacher")
                .password(passwordEncoder.encode("password"))
                .roles("TEACHER")
                .build();

        return new InMemoryUserDetailsManager(admin, teacher);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disabilita CSRF per le API REST
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/rooms/create_room").hasRole("ADMIN")
                        .requestMatchers("/api/rooms/delete/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/rooms/update/{id}").hasRole("ADMIN")
                        .requestMatchers("/api/rooms/**").permitAll()
                        .anyRequest().authenticated()
                )
                .httpBasic(httpBasic -> {});

        return http.build();
    }
}