package com.example.project.project.security;

import com.example.project.project.handler.AuthenticationFailureHandler;
import com.example.project.project.filter.CustomAuthenticationFilter;
import com.example.project.project.filter.CustomAuthorizationFilter;
import com.example.project.project.handler.CustomLogoutHandler;
import com.example.project.project.service.LogUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.project.project.security.SecurityParameter.*;

@EnableWebSecurity
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final AuthenticationFailureHandler restAuthenticationFailurehandler;
    private final CustomLogoutHandler logoutHandler;
    private final LogUrlService logUrlService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(), logUrlService);
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        customAuthenticationFilter.setFilterProcessesUrl(LOGIN_URL);
        http.csrf().disable();
        http.authorizeHttpRequests().antMatchers(LOGIN_URL).permitAll();
        http.authorizeHttpRequests().antMatchers(SIGNUP_URL).permitAll();
        http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(customAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
        http.logout().logoutUrl(LOGOUT_URL).logoutSuccessHandler(logoutHandler);
        http.exceptionHandling().authenticationEntryPoint(restAuthenticationFailurehandler);
    }

    @Bean
    public CustomAuthorizationFilter customAuthorizationFilter() {
        return new CustomAuthorizationFilter();
    }

    @Bean
    public FilterRegistrationBean<CustomAuthorizationFilter> myFilterRegistationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new CustomAuthorizationFilter());
        filterRegistrationBean.setEnabled(false);

        return filterRegistrationBean;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}