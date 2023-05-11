package com.airbyte.dorm.security;

import com.airbyte.dorm.manager.ManagerService;
import com.airbyte.dorm.supervisor.SupervisorService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final NoOpPasswordEncoder noOpPasswordEncoder;
    private final ManagerService managerService;
    private final SupervisorService supervisorService;

    public SecurityConfig(NoOpPasswordEncoder bCryptPasswordEncoder, ManagerService managerService, SupervisorService supervisorService) {
        this.noOpPasswordEncoder = bCryptPasswordEncoder;
        this.managerService = managerService;
        this.supervisorService = supervisorService;
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(managerService).passwordEncoder(noOpPasswordEncoder);
        auth.userDetailsService(supervisorService).passwordEncoder(noOpPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/login" , "/api/v1/manager/token/refresh" , "/api/v1/supervisor/token/refresh", "/api/v1/email/forgot/password").permitAll();
        http.authorizeRequests().antMatchers("/api/**").hasAnyAuthority("MANAGER", "SUPERVISOR");
        //http.authorizeRequests().antMatchers("/api/v1/**").hasAnyAuthority("SUPERVISOR", "MANAGER");
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean(), managerService, supervisorService));
        http.addFilterBefore(new CustomAuthorizationFilter() , UsernamePasswordAuthenticationFilter.class);
        http.logout().permitAll();
        // cors config :
        // if allowed credentials is true : we should give exact origin address in allowed origins
        // if allowed credentials is false : we can give patterns for allowed origins, for example "*"
        http.cors(request -> {
            CorsConfigurationSource ccs = x -> {
                CorsConfiguration corsConfiguration = new CorsConfiguration();
                // "https://www.saadatportal.com"
//                corsConfiguration.setAllowedOrigins(List.of("https://www.saadatportal.com"));
                corsConfiguration.setAllowedOrigins(List.of("*"));
                corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
//                corsConfiguration.setAllowCredentials(true);
                corsConfiguration.setAllowCredentials(false);
                corsConfiguration.setAllowedHeaders(List.of("Content-Type", "Authorization", "Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "accessToken", "refreshToken", "role", "Access-Control-Allow-Methods", "Access-Control-Allow-Method", "id", "fullName"));
                corsConfiguration.setExposedHeaders(List.of("Content-Type", "Authorization", "accessToken", "refreshToken", "role", "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "Access-Control-Allow-Method", "id", "fullName"));
                return corsConfiguration;
            };
            request.configurationSource(ccs);
        });
    }


}

