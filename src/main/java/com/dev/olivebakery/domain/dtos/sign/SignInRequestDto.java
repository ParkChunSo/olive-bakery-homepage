package com.dev.olivebakery.domain.dtos.sign;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignInRequestDto {

  private String id;
  private String pw;
}
