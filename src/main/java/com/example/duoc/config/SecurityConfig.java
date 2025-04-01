package com.example.duoc.config;

import com.example.duoc.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.StaticHeadersWriter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Bean
    public MethodSecurityExpressionHandler methodSecurityExpressionHandler() {
        return new DefaultMethodSecurityExpressionHandler();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String REPORT_TO = "{\"group\":\"csp-violation-report\",\"max_age\":2592000,\"endpoints\":[{\"url\":\"https://localhost:8080/report\"}]}";

    http.authorizeHttpRequests(
            (requests) ->
                requests
                    .requestMatchers(
                        "/",
                        "/home",
                        "/search",
                        "/login",
                        "/logout-success",
                        "/css/**",
                        "/js/**",
                        "/images/**",
                        "/h2-console/**")
                    .permitAll()
                    .requestMatchers("/recipes/**")
                    .authenticated()
                    .requestMatchers("/users", "/me")
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .formLogin((form) -> form.loginPage("/login").permitAll().defaultSuccessUrl("/home", true))
        .logout((logout) -> logout.permitAll().logoutSuccessUrl("/logout-success"))
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
        .headers(
            headers ->
                headers
                    .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)
                    .xssProtection(Customizer.withDefaults())
                    .contentSecurityPolicy(
                        contentSecurityPolicyConfig ->
                            contentSecurityPolicyConfig.policyDirectives(
                                "form-action 'self'; script-src 'self'; script-src 'self'")));

        return http.build();
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
