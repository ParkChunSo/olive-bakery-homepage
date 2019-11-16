package com.dev.olivebakery.domain.dtos.sign;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberListResponseDto {

  private String id;
  private String name;
  private String phoneNumber;
  private int age;
  private boolean isMale;
  private int stamp;

  @Builder
  public MemberListResponseDto(String id, String name, String phoneNumber, int age, boolean isMale,
      int stamp) {
    this.id = id;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.age = age;
    this.isMale = isMale;
    this.stamp = stamp;
  }
}
