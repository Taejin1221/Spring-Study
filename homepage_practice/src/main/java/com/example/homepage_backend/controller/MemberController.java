package com.example.homepage_backend.controller;

import com.example.homepage_backend.domain.Member;
import com.example.homepage_backend.domain.dto.request.MemberJoinRequest;
import com.example.homepage_backend.domain.dto.request.MemberLoginRequest;
import com.example.homepage_backend.domain.dto.response.MemberResponse;
import com.example.homepage_backend.domain.dto.response.ResponseDTO;
import com.example.homepage_backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public MemberController(PasswordEncoder passwordEncoder, MemberService memberService, AuthenticationManager authenticationManager) {
        this.passwordEncoder = passwordEncoder;
        this.memberService = memberService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO<MemberResponse>> joinMember(@RequestBody MemberJoinRequest req) {
        try {
            MemberResponse res = new MemberResponse(
                    memberService.join(
                            new Member(req.getEmail(), passwordEncoder.encode(req.getPassword()), req.getNickname())
                    )
            );

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("환영합니다, %s님. 회원가입이 완료되었습니다.", res.getNickname()),
                            res
                    ), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            e.getMessage(),
                            null
                    ), HttpStatus.OK);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<MemberResponse>> login(@RequestBody MemberLoginRequest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = memberService.getMemberByEmail(authentication.getName());

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("안녕하세요, %s님. 로그인이 완료되었습니다.", member.getNickname()),
                            new MemberResponse(member)
                    ), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            e.getMessage(),
                            null
                    ), HttpStatus.OK);
        }
    }
}
