package com.dev.olivebakery.domain.entity;

import com.dev.olivebakery.domain.dtos.sign.SignUpRequestDto;
import com.dev.olivebakery.domain.enums.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_tbl")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uuid;

    @Column(unique = true)
    private String id;

    private String pw;

    private String name;

    @Column(name = "phone_num")
    private String phoneNumber;

    // true면 남자
    private boolean isMale;

    private int age;

    // 10000원 이상 구매시 +1
    private int stamp;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private Set<MemberRole> role;

    @OneToMany(mappedBy = "member")
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "member" ,fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @Builder
    public Member(String id, String pw, String name, String phoneNumber,boolean isMale, int age, int stamp){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.isMale = isMale;
        this.age = age;
        this.stamp = stamp;
    }

    public User toUser(){
        return new User(id, pw
                , role.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                    .collect(Collectors.toSet()));
    }

    public Member update(SignUpRequestDto dto){
        this.id = dto.getId();
        this.name = dto.getName();
        this.pw = dto.getPw();
        this.phoneNumber = dto.getPhoneNumber();
        
        return this;
    }
}
