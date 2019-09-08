package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.sign.MemberDetailsResponseDto;
import com.dev.olivebakery.domain.dtos.sign.MemberListResponseDto;
import com.dev.olivebakery.domain.dtos.sign.SignInRequestDto;
import com.dev.olivebakery.domain.dtos.sign.SignUpRequestDto;
import com.dev.olivebakery.domain.enums.MemberRole;
import com.dev.olivebakery.security.JwtProvider;
import com.dev.olivebakery.service.signService.SignService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/olive/sign")
public class SignController {
    @Autowired
    JwtProvider jwtProvider;

    private final SignService signService;

    public SignController(SignService signService) {
        this.signService = signService;
    }

    @ApiOperation("관리자 회원가입")
    @PostMapping("/admin")
    public String signUpAdmin(@RequestBody SignUpRequestDto signupDto){
        return signService.signUp(signupDto, MemberRole.ADMIN.name());
    }

    @ApiOperation("일반 회원가입")
    @PostMapping("/client")
    public String signUpClient(@RequestBody SignUpRequestDto signupDto){
        return signService.signUp(signupDto, MemberRole.CLIENT.name());
    }

    @ApiOperation("로그인")
    @PostMapping
    public String signIn(@RequestBody SignInRequestDto signInDto){
        return signService.signIn(signInDto);
    }

    @ApiOperation("회원정보 수정")
    @PutMapping
    public void update(@RequestBody SignUpRequestDto signupDto){
        signService.update(signupDto);
    }

    @ApiOperation("회원정보 삭제")
    @DeleteMapping
    public void delete(@RequestBody SignInRequestDto signInDto){
        signService.delete(signInDto);
    }

//    @ApiOperation("회원정보 조회")
//    @GetMapping("/userId/{userId:.+}/")
//    public SignDto.MemberDto getMember(@PathVariable String userId){
//        return signService.getMemberDetailsByToken(userId);
//    }

    @ApiOperation(value = "회원정보 조회" , notes = "토큰을 이용한 해당 유저 개인정보 요청")
    @GetMapping("/check")
    public MemberDetailsResponseDto getMemberInfoByToken(@RequestHeader(name = "Authorization") String bearerToken){
        return signService.getMemberDetailsByToken(bearerToken);
    }

    @ApiOperation(value = "회원정보 조회" , notes = "권한을 이용한 해당 유저 개인정보 요청(관리자만 가능)")
    @GetMapping("/{userId}")
    public MemberDetailsResponseDto getMemberInfoByPermission(@PathVariable String userId){
        return signService.getMemberDetailsByRole(userId);
    }

    @ApiOperation("전체 회원정보 조회")
    @GetMapping("/members")
    public List<MemberListResponseDto> getWholeMembers(){
        return signService.getMembersInfo();
    }
}
