package com.solarsan.sabrage.security;

import static com.solarsan.sabrage.security.Permission.PLAYER_ACTIVATE;
import static com.solarsan.sabrage.security.Permission.PLAYER_GET_ALL;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_APPLY;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_CREATE;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_SET_RESULT;
import static com.solarsan.sabrage.security.Permission.TOURNAMENT_UPDATE;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(final AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .authorizeHttpRequests()
            // tournament get all & get -> open
            .antMatchers(HttpMethod.GET, "/tournaments", "/tournaments/{name}").permitAll()
            // tournament create
            .antMatchers(HttpMethod.POST, "/tournaments").hasAuthority(TOURNAMENT_CREATE.getPermission())
            // tournament update
            .antMatchers(HttpMethod.PUT, "/tournaments/{name}").hasAuthority(TOURNAMENT_UPDATE.getPermission())
            // tournament apply
            .antMatchers(HttpMethod.POST, "/tournaments/{name}/participants").hasAuthority(TOURNAMENT_APPLY.getPermission())
            // tournament set result
            .antMatchers(HttpMethod.PATCH, "/tournaments/{name}/games/{id}").hasAuthority(TOURNAMENT_SET_RESULT.getPermission())
            // player create -> open
            .antMatchers(HttpMethod.POST, "/players").permitAll()
            // login -> open
            .antMatchers(HttpMethod.POST, "/auth").permitAll()
            // player get all
            .antMatchers(HttpMethod.GET, "/players").hasAuthority(PLAYER_GET_ALL.getPermission())
            // player activate
            .antMatchers(HttpMethod.PATCH, "/players/{email}").hasAuthority(PLAYER_ACTIVATE.getPermission())
            .anyRequest().authenticated()
            .and().httpBasic()
            .and().logout()
            .and().cors().and().csrf().disable()
            .build();
    }

    @Bean
    @Profile("local")
    public CorsFilter corsFilter(@Value("${app.cors.allowed-origins}") final List<String> allowedOrigins) {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOriginPatterns(allowedOrigins);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");

        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
