package com.example.intermediate.jwt;

import com.example.intermediate.domain.dto.ResponseDto;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AccessDeniedHandlerException implements AccessDeniedHandler {

  @SneakyThrows
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) {
    throw ResponseDto.fail("FORBIDDEN", "forbidden");
  }
}
