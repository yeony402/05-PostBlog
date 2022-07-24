package com.example.intermediate.domain.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {

  @NotBlank
  @Size(min = 4, max = 12)
  @Pattern(regexp = "^|a-zA-Z0-9]*${3,12}")
  private String nickname;

  @NotBlank
  @Size(min = 4, max = 32)
  @Pattern(regexp = "^[a-z][0-9]*${3,32}")
  private String password;

  @NotBlank
  private String passwordConfirm;
}
