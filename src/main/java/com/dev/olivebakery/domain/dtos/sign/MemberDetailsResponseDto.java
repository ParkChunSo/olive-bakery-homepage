package com.dev.olivebakery.domain.dtos.sign;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberDetailsResponseDto {

  private String id;
  private String name;
  private String phoneNumber;
  private int age;
  private boolean isMale;
  private int stamp;

  //TODO 추천로직을 넣게되면 해당 멤버의 여러가지 성향 등 추가될 듯.

  @Builder
  public MemberDetailsResponseDto(String id, String name, String phoneNumber, int age,
      boolean isMale, int stamp) {
    this.id = id;
    this.name = name;
    this.phoneNumber = phoneNumber;
    this.age = age;
    this.isMale = isMale;
    this.stamp = stamp;
  }
}
