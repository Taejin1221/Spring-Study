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

            return new ResponseEntity<>(new ResponseDTO<>("회원가입이 완료되었습니다.", result), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ResponseDTO<>(e.getMessage(), null), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<Member>> login(@RequestBody MemberLoginRequest memberLoginRequest) {
        try {
            if (memberService.login(memberLoginRequest.getEmail(), memberLoginRequest.getPassword())) {
                return new ResponseEntity<>(new ResponseDTO<>("로그인 완료", null), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ResponseDTO<>("예상치 못한 오류입니다. 관리자에게 문의하세요.", null), HttpStatus.NOT_FOUND);
            }
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(new ResponseDTO<>(e.getMessage(), null), HttpStatus.OK);
        }
    }
}
