package com.callicoder.config;

import com.callicoder.config.jwt.JwtAuthenticationEntryPoint;
import com.callicoder.config.jwt.JwtAuthenticationFilter;
import com.callicoder.config.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/",
                            "/test",
                            "/favicon.ico",
                            "/**/*.png",
                            "/**/*.gif",
                            "/**/*.svg",
                            "/**/*.jpg",
                            "/**/*.html",
                            "/**/*.css",
                            "/**/*.js")
                    .permitAll()
                    .antMatchers("/api/auth/**")
                    .permitAll()
                    .antMatchers("/api/user/checkUsernameAvailability", "/api/user/checkEmailAvailability")
                    .permitAll()
                    .antMatchers(HttpMethod.GET, "/api/polls/**", "/api/users/**" , "/test")
                    .permitAll()
                    .anyRequest()
                    .authenticated()
                    .and()
                    .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                    .and()
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtAuthenticationFilter() , UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}
