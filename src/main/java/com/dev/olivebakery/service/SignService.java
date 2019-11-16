package com.dev.olivebakery.service;

import com.dev.olivebakery.domain.dtos.sign.MemberDetailsResponseDto;
import com.dev.olivebakery.domain.dtos.sign.MemberListResponseDto;
import com.dev.olivebakery.domain.dtos.sign.SignInRequestDto;
import com.dev.olivebakery.domain.dtos.sign.SignUpRequestDto;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.exception.UserDefineException;
import com.dev.olivebakery.repository.MemberRepository;
import com.dev.olivebakery.security.JwtProvider;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Log
public class SignService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public SignService(MemberRepository memberRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    /**
     * 로그인 후 토큰 전송
     *
     * @param signInDto : 로그인 정보
     * @return Token
     */
    public String signIn(SignInRequestDto signInDto){

        log.info("----login --- " + signInDto.getId() + "  " + signInDto.getPw());
        Member member = memberRepository.findById(signInDto.getId())
                      .orElseThrow(() -> new UserDefineException("아이디를 잘못 입력하셨습니다."));

        if(!passwordEncoder.matches(signInDto.getPw(), member.getPw())){
          throw new UserDefineException("비밀번호를 잘못 입력하셨습니다.");
        }

        return jwtProvider.createToken(member.getId(), member.getRole());
    }

    /**
     * 회원가입 후 토큰 전송
     *
     * @param signupDto : 회원가입 정보
     * @param ROLE : 회원의 role
     * @return Token
     */
    public String signUp(SignUpRequestDto signupDto, String ROLE) {
      if (memberRepository.findById(signupDto.getId()).isPresent()) {
        throw new UserDefineException("아이디가 중복됩니다.");
      }

      Member member = signupDto.toEntity();
      member.setPw(passwordEncoder.encode(member.getPw()));
      if (ROLE.equals(MemberRole.ADMIN.name())) {
        member.setRole(Stream.of(MemberRole.ADMIN, MemberRole.CLIENT).collect(Collectors.toSet()));
      } else {
        member.setRole(Stream.of(MemberRole.CLIENT).collect(Collectors.toSet()));
      }
      memberRepository.save(member);

      return jwtProvider.createToken(member.getId(), member.getRole());
    }

    /**
     * 회원정보 수정
     *
     * @param signupDto : 회원정보
     */
    public void update(SignUpRequestDto signupDto) {
      Member member = memberRepository.findById(signupDto.getId())
                    .orElseThrow(() -> new UserDefineException("아이디가 존재하지 않습니다."));
      signupDto.updatePassword(passwordEncoder.encode(signupDto.getPw()));
      memberRepository.save(member.update(signupDto));
    }

    /**
     * 회원 탈퇴
     *
     * @param signInDto: 회원정보
     */
    public void delete(SignInRequestDto signInDto) {
      Member member = memberRepository.findById(signInDto.getId())
                    .orElseThrow(() -> new UserDefineException("아이디를 잘못 입력하셨습니다."));
      if (passwordEncoder.matches(signInDto.getPw(), member.getPw())) {
        memberRepository.deleteById(member.getUuid());
      } else {
        throw new UserDefineException("비밀번호를 잘못 입력하셨습니다.");
      }
    }

    /**
     * 토큰을 사용한 회원의 상세정보 조회
     *
     * @param bearerToken : 회원 토큰
     * @return 회원정보
     */
    public MemberDetailsResponseDto getMemberDetailsByToken(String bearerToken) {
      Member member = memberRepository.findById(jwtProvider.getUserEmailByToken(bearerToken))
                    .orElseThrow(() -> new UserDefineException("아이디가 존재하지 않습니다."));

      return MemberDetailsResponseDto.builder()
          .id(member.getId())
          .name(member.getName())
          .phoneNumber(member.getPhoneNumber())
          .age(member.getAge())
          .isMale(member.isMale())
          .stamp(member.getStamp())
          .build();
    }

    /**
     * 권한을 사용한 회원의 상세정보 조회
     *
     * @param userId: 조회할 사용자 아이디
     * @return 회원정보
     */
    public MemberDetailsResponseDto getMemberDetailsByRole(String userId) {
      Member member = memberRepository.findById(userId)
                    .orElseThrow(() -> new UserDefineException("아이디가 존재하지 않습니다."));

      return MemberDetailsResponseDto.builder()
          .id(member.getId())
          .name(member.getName())
          .phoneNumber(member.getPhoneNumber())
          .age(member.getAge())
          .isMale(member.isMale())
          .stamp(member.getStamp())
          .build();
    }

    /**
     * 모든 회원정보 조회
     *
     * @return 회원리스트
     */
    public List<MemberListResponseDto> getMembersInfo() {
      List<Member> members = memberRepository.findAll();
      List<MemberListResponseDto> memberDtos = new ArrayList<>();
      members.forEach(member -> {
        memberDtos.add(MemberListResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .age(member.getAge())
                .isMale(member.isMale())
                .stamp(member.getStamp())
                .build()
        );
      });
      return memberDtos;
    }

  public Member findById(String userId) {
    return memberRepository.findById(userId)
        .orElseThrow(() -> new UserDefineException("해당 유저가 존재하지 않습니다."));
  }
}
