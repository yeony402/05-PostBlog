package com.example.intermediate.service;

import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.RefreshToken;
import com.example.intermediate.domain.UserDetailsImpl;
import com.example.intermediate.domain.dto.LoginRequestDto;
import com.example.intermediate.domain.dto.MemberRequestDto;
import com.example.intermediate.domain.dto.ResponseDto;
import com.example.intermediate.domain.dto.TokenDto;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.MemberRepository;
import com.example.intermediate.repository.RefreshTokenRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManagerBuilder authenticationManagerBuilder;

  private final TokenProvider tokenProvider;

  @Transactional
  public ResponseDto<?> createMember(MemberRequestDto requestDto) {
    if (null != isPresentMember(requestDto.getNickname())) {
      return ResponseDto.fail("DUPLICATED_NICKNAME",
          "nickname is duplicated");
    }

    if (!requestDto.getPassword().equals(requestDto.getPasswordConfirm())) {
      return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
          "password and password confirm are not matched");
    }

    Member member = new Member(requestDto, passwordEncoder);
    memberRepository.save(member);
    return ResponseDto.success(member);
  }

  @Transactional
  public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
    Member member = isPresentMember(requestDto.getNickname());
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "member not found");
    }

    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(requestDto.getNickname(), requestDto.getPassword());
    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

    RefreshToken refreshToken = RefreshToken.builder()
        .id(member.getId())
        .value(tokenDto.getRefreshToken())
        .build();

    refreshTokenRepository.save(refreshToken);
    tokenToHeaders(tokenDto, response);
    return ResponseDto.success(member);
  }

  @Transactional
  public ResponseDto<?> reissue(Long id, HttpServletRequest request, HttpServletResponse response) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }

    Authentication authentication = tokenProvider.getAuthentication(request.getHeader("Access-Token"));
    RefreshToken refreshToken = isPresentRefreshToken(id);

    if (!refreshToken.getValue().equals(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }

    TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
    refreshToken.updateValue(tokenDto.getRefreshToken());
    tokenToHeaders(tokenDto, response);
    return ResponseDto.success("reissue success");
  }

  public ResponseDto<?> logout(HttpServletRequest request) {
    if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
      return ResponseDto.fail("INVALID_TOKEN", "refresh token is invalid");
    }
    Member member = getMemberFromAuthentication();
    if (null == member) {
      return ResponseDto.fail("MEMBER_NOT_FOUND",
          "member not found");
    }

    RefreshToken refreshToken = isPresentRefreshToken(member.getId());
    refreshTokenRepository.delete(refreshToken);
    return ResponseDto.success("logout success");
  }

  @Transactional(readOnly = true)
  public Member isPresentMember(String nickname) {
    Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
    return optionalMember.orElse(null);
  }

  @Transactional(readOnly = true)
  public RefreshToken isPresentRefreshToken(Long id) {
    Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(id);
    return optionalRefreshToken.orElse(null);
  }

  public Member getMemberFromAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || AnonymousAuthenticationToken.class.
        isAssignableFrom(authentication.getClass())) {
      return null;
    }
    return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
  }

  public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
    response.addHeader("Access-Token", "Bearer " + tokenDto.getAccessToken());
    response.addHeader("Refresh-Token", "Bearer " + tokenDto.getRefreshToken());
    response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
  }

}
