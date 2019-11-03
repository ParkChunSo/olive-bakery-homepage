package com.dev.olivebakery.domain.dtos.sign;

import com.dev.olivebakery.domain.entity.Member;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class SignUpRequestDto {
    private String id;
    private String pw;
    private String name;
    private String phoneNumber;
    private int age;
    @JsonProperty
    private boolean isMale;

    @Builder
    public SignUpRequestDto(String id, String pw, String name, String phoneNumber, int age, boolean isMale) {
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.isMale = isMale;
    }

    public Member toEntity(){
        return Member.builder()
                .id(id)
                .pw(pw)
                .name(name)
                .phoneNumber(phoneNumber)
                .isMale(isMale)
                .age(age)
                .stamp(0)
                .build();
    }

    public SignUpRequestDto updatePassword(String pw){
        this.pw = pw;
        return this;
    }
}
