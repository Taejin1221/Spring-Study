package com.example.homepage_backend.controller;

import com.example.homepage_backend.domain.Member;
import com.example.homepage_backend.repository.MemberRepository;
import com.example.homepage_backend.service.MemberService;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(MemberController.class)
class MemberControllerTest {

    @Autowired MockMvc mockMvc;
    @MockBean MemberService memberService;
    @MockBean PasswordEncoder passwordEncoder;
    @MockBean AuthenticationManager authenticationManager;

    Member testMember;
    private final Gson gson = new Gson();

    @BeforeEach
    void setUp() {
        testMember = new Member("taejin7824@gmail.com", "1234", "taejin");
    }

    @Test
    void testJoin_Success() throws Exception {
        Mockito.when(memberService.join(any(Member.class))).thenReturn(testMember);

        mockMvc.perform(MockMvcRequestBuilders.post("/member/join")
                        .with(csrf())
                        .with(user("taejin7824@gmail.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testMember)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("환영합니다, " + testMember.getNickname() + "님. 회원가입이 완료되었습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(testMember.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value(testMember.getNickname()));
    }

    @Test
    void testJoin_Failure_EmailAlreadyExists() throws Exception {
        Mockito.when(memberService.join(any(Member.class))).thenThrow(new IllegalStateException("이미 존재하는 이메일입니다."));

        Member dupEmail = new Member("taejin7824@gmail.com", "1234", "gene");
        mockMvc.perform(MockMvcRequestBuilders.post("/member/join")
                        .with(csrf())
                        .with(user("taejin7824@gmail.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dupEmail)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    void testJoin_Failure_NicknameAlreadyExists() throws Exception {
        Mockito.when(memberService.join(any(Member.class))).thenThrow(new IllegalStateException("이미 존재하는 닉네임입니다."));

        Member dupEmail = new Member("taejin7824@gmail.com", "1234", "gene");
        mockMvc.perform(MockMvcRequestBuilders.post("/member/join")
                        .with(csrf())
                        .with(user("taejin7824@gmail.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(dupEmail)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("이미 존재하는 닉네임입니다."));
    }

    @Test
    void testLogin_Success() throws Exception {
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                testMember.getEmail(),
                testMember.getPassword(),
                new ArrayList<>());
        Mockito.when(authenticationManager.authenticate(any())).thenReturn(authentication);
        Mockito.when(memberService.getMemberByEmail(any())).thenReturn(testMember);

        mockMvc.perform(MockMvcRequestBuilders.post("/member/login")
                        .with(csrf())
                        .with(user("taejin7824@gmail.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testMember)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("안녕하세요, " + testMember.getNickname() + "님. 로그인이 완료되었습니다."))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.email").value(testMember.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname").value(testMember.getNickname()));
    }

    @Test
    void testLogin_Failure_InvalidCredentials() throws Exception {
        Mockito.when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Invalid credentials"));

        mockMvc.perform(MockMvcRequestBuilders.post("/member/login")
                        .with(csrf())
                        .with(user("taejin7824@gmail.com").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(testMember)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid credentials"));
    }
}