package com.example.intermediate.jwt;

import com.example.intermediate.domain.dto.ResponseDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPointException implements
    AuthenticationEntryPoint {

  @SneakyThrows
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) {
    throw ResponseDto.fail("UNAUTHORIZE", "unauthorize");
  }
}
