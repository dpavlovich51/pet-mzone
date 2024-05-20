package com.mzone.main.config;

import com.mzone.main.security.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.*;
import org.springframework.security.web.authentication.*;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
// to enable @PreAuthorize("userRole:admin")
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenFilter jwtTokenFilter;
    private final UserDetailsService userService;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .exceptionHandling()
                .authenticationEntryPoint((request, response, authException) ->
                        response.setStatus(HttpStatus.NOT_FOUND.value()))
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        response.setStatus(HttpStatus.FORBIDDEN.value()));

        http
//                .cors().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http
                .authorizeRequests()

                .antMatchers("/test/**").permitAll()

                .antMatchers("/").permitAll()
                .antMatchers("/hello-user").authenticated()

                .antMatchers("/v1/sing-up/**").permitAll()

                .antMatchers("/user/sing-in-with-email").permitAll()
                .antMatchers("/user/sing-in-with-refresh-token").permitAll()
                .antMatchers("/user/sing-in-with-google").permitAll()

                .anyRequest()
                .authenticated();

        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider)
                .userDetailsService(userService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

}
