package com.example.homepage_practice.controller;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.domain.dto.request.MemberJoinRequest;
import com.example.homepage_practice.domain.dto.request.MemberLoginRequest;
import com.example.homepage_practice.domain.dto.response.ResponseDTO;
import com.example.homepage_practice.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/member")
public class MemberController {
    MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/join")
    public ResponseEntity<ResponseDTO<Member>> joinMember(@RequestBody MemberJoinRequest memberJoinRequest) {
        try {
            Member result = memberService.join(new Member(memberJoinRequest.getEmail(), memberJoinRequest.getPassword(), memberJoinRequest.getNickname()));

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("환영합니다, %s님. 회원가입이 완료되었습니다.", result.getNickname()),
                            result
                    ), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            e.getMessage(),
                            null
                    ), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Member>> login(@RequestBody MemberLoginRequest memberLoginRequest) {
        try {
            String nickname = memberService.login(memberLoginRequest.getEmail(), memberLoginRequest.getPassword());
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("안녕하세요, %s님. 로그인이 완료되었습니다.", nickname),
                            null
                    ), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            e.getMessage(),
                            null
                    ), HttpStatus.OK);
        }
    }
}
