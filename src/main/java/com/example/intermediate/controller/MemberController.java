package com.example.intermediate.controller;

import com.example.intermediate.domain.dto.LoginRequestDto;
import com.example.intermediate.domain.dto.MemberRequestDto;
import com.example.intermediate.domain.dto.ResponseDto;
import com.example.intermediate.service.MemberService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MemberController {

  private final MemberService memberService;

  @PostMapping("/api/member/signup")
  public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
    return memberService.createMember(requestDto);
  }

  @PostMapping("/api/member/login")
  public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
      HttpServletResponse response
  ) {
    return memberService.login(requestDto, response);
  }

  @PostMapping("/api/member/reissue/{id}")
  public ResponseDto<?> login(@PathVariable Long id,
      HttpServletRequest request,
      HttpServletResponse response
  ) {
    return memberService.reissue(id, request, response);
  }

  @PostMapping("/api/member/logout")
  public ResponseDto<?> logout(HttpServletRequest request) {
    return memberService.logout(request);
  }
}
