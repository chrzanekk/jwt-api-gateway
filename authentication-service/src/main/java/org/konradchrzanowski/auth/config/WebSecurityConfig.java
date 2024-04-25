package org.konradchrzanowski.auth.config;

import org.konradchrzanowski.auth.security.AuthTokenFilter;
import org.konradchrzanowski.auth.security.SecurityProblemSupport;
import org.konradchrzanowski.auth.security.services.UserDetailsServiceImpl;
import org.konradchrzanowski.utils.common.enumeration.ERole;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    UserDetailsServiceImpl userDetailsService;

    private final SecurityProblemSupport securityProblemSupport;


    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService,
                             SecurityProblemSupport securityProblemSupport) {
        this.userDetailsService = userDetailsService;
        this.securityProblemSupport = securityProblemSupport;
    }

    @Bean
    public AuthTokenFilter authenticationTJwtTokenFilter() {
        return new AuthTokenFilter();
    }


    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//todo need to reimplement filterChain for spring boot 3
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()
                .exceptionHandling().authenticationEntryPoint(securityProblemSupport)
                .accessDeniedHandler(securityProblemSupport)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/test//all").permitAll()
                .antMatchers("/api/test/user").hasAnyAuthority(ERole.ROLE_USER.getRoleName())
                .antMatchers("/api/test/mod").hasAnyAuthority(ERole.ROLE_MODERATOR.getRoleName())
                .antMatchers("/api/test/admin").hasAnyAuthority(ERole.ROLE_ADMIN.getRoleName())
                .antMatchers("/api/tires/**").hasAnyAuthority(ERole.ROLE_ADMIN.getRoleName(),ERole.ROLE_USER.getRoleName())
                .antMatchers("/api/account/**").authenticated();

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationTJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }


}
