package com.dev.olivebakery.controller;

import com.dev.olivebakery.domain.dtos.sign.SignUpRequestDto;
import com.dev.olivebakery.domain.entity.Member;
import com.dev.olivebakery.repository.MemberRepository;
import com.dev.olivebakery.security.JwtProvider;
import com.dev.olivebakery.service.SignService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class SignControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @MockBean
    private SignService signService;
    private ObjectMapper om = new ObjectMapper();
    private String userName = "signTest@id.com";
    private String password = "1234";
    private String name = "박춘소";
    private String phoneNumber = "010-0000-0000";
    private String token;


    @Before
    public void init(){
        Member member = memberRepository.findById(userName).orElse(null);
        if(member != null)
            memberRepository.delete(member);
    }

    @Test
    public void signUpAdmin() throws Exception {
        mvc.perform(post("/olive/sign/client")
                .content(om.writeValueAsString(

                        SignUpRequestDto.builder()
                                .id(userName)
                                .pw(password)
                                .name(name)
                                .phoneNumber(phoneNumber)
                                .build()
                        )
                    )
                    .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void signUpClient() throws Exception{
        mvc.perform(post("/olive/sign/client")
                        .content(om.writeValueAsString(

                                SignUpRequestDto.builder()
                                    .id(userName)
                                    .pw(password)
                                    .name(name)
                                    .phoneNumber(phoneNumber)
                                    .build()
                            )
                        )
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
        ;
    }

    @Test
    public void signIn() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void getMember() {
    }

    @Test
    public void getWholeMembers() {
    }

    @Test
    public void test(){
    }
}