package dev.carpenter.sbrmstack.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import dev.carpenter.sbrmstack.security.jwt.UserEntryPointJwt;
import dev.carpenter.sbrmstack.security.jwt.UserTokenFilter;
import dev.carpenter.sbrmstack.security.services.UserDetailsServiceImpl;

@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
//(securedEnabled = true,
//jsr250Enabled = true,
//prePostEnabled = true) // by default
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    private UserEntryPointJwt unauthorizedHandler;

    @Bean
    public UserTokenFilter authenticationJwtTokenFilter() {
        return new UserTokenFilter();
    }

//@Override
//public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//  authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
//}

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//@Bean
//@Override
//public AuthenticationManager authenticationManagerBean() throws Exception {
//  return super.authenticationManagerBean();
//}

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()  // Allow all requests to /api/auth/**
                        .requestMatchers("/api/test/**").permitAll()  // Optionally allow other public endpoints
                        .anyRequest().authenticated()  // Require authentication for any other request
                )
                .authenticationProvider(authenticationProvider())  // Provide custom authentication provider
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);  // Add JWT filter before UsernamePasswordAuthenticationFilter

        return http.build();
    }
}