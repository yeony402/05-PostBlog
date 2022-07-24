package com.example.intermediate.configuration;

import com.example.intermediate.jwt.AccessDeniedHandlerException;
import com.example.intermediate.jwt.AuthenticationEntryPointException;
import com.example.intermediate.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.security.ConditionalOnDefaultWebSecurity;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ConditionalOnDefaultWebSecurity
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class SecurityConfiguration {

  private final TokenProvider tokenProvider;
  private final AuthenticationEntryPointException authenticationEntryPointException;
  private final AccessDeniedHandlerException accessDeniedHandlerException;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  @Order(SecurityProperties.BASIC_AUTH_ORDER)
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors();

    http.csrf().disable()

        // exception handlingd에 직접 만든 401, 403 class 추가
        .exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPointException)
        .accessDeniedHandler(accessDeniedHandlerException)

        // Spring Security는 기본적으로 session 사용
        // but 본 로직에서는 session을 사용하지 않으므로 STATELESS 처리
        .and()
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

        .and()
        .authorizeRequests()
        .antMatchers("/api/member/signup").permitAll()
        .antMatchers("/api/member/login").permitAll()
        .anyRequest().authenticated()

        .and()
        .apply(new JwtSecurityConfiguration(tokenProvider));

    return http.build();
  }
}
