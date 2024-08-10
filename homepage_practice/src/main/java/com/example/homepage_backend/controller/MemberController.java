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
    public ResponseEntity<ResponseDTO<MemberResponse>> joinMember(@RequestBody MemberJoinRequest req) {
        try {
            MemberResponse res = new MemberResponse(
                    memberService.join(
                            new Member(req.getEmail(), req.getPassword(), req.getNickname())
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
                    ), HttpStatus.NOT_ACCEPTABLE);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<MemberResponse>> login(@RequestBody MemberLoginRequest req) {
        try {
            MemberResponse res = new MemberResponse(
                    memberService.login(req.getEmail(), req.getPassword())
            );

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("안녕하세요, %s님. 로그인이 완료되었습니다.", res.getNickname()),
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
}
