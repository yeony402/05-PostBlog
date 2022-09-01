package com.example.intermediate.jwt;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEntryPointException implements
    AuthenticationEntryPoint {

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException {
//<<<<<<< HEAD
//    response.setContentType("application/json;charset=UTF-8");
//    response.getWriter().println(
//        new ObjectMapper().writeValueAsString(
//            ResponseDto.fail("BAD_REQUEST", "로그인이 필요합니다.")
//        )
//    );
//    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//=======
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthorized");
  }
}
