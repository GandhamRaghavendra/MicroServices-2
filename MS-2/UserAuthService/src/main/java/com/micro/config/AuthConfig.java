package com.micro.config;

import com.micro.filter.AdminFilter;
import com.micro.filter.BasicFilter;
import com.micro.filter.JwtValidationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AuthConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        HttpSecurity httpSecurity = http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auths ->
                        auths
                            .requestMatchers("/auths/register")
                            .permitAll()
                            .requestMatchers("/auths/admin","/auths/all/**")
                            .hasAuthority("ADMIN")
                            .anyRequest()
                            .authenticated()

                )
                .addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                ;

        return httpSecurity.build();
    }

    @Bean
    public FilterRegistrationBean<BasicFilter> customBasicFilter() {
        FilterRegistrationBean<BasicFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new BasicFilter());
        registrationBean.addUrlPatterns("/auths/**");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AdminFilter> customAdminFilter() {
        FilterRegistrationBean<AdminFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AdminFilter());
        registrationBean.addUrlPatterns("/auths/all/**");
        return registrationBean;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}