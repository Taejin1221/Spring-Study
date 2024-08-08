package com.example.homepage_practice.service;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void join() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberService.join(member);

        Member result = memberRepository.findByNickname("taejin").get();
        Assertions.assertThat(member).isEqualTo(result);

        Member dupEmail = new Member("taejin7824@gmail.com", "qwer", "gene");
        IllegalStateException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(dupEmail);
        });
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");

        Member dupNickname = new Member("taejin7824@kakao.com", "qwer", "taejin");
        e = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.join(dupNickname);
        });
        Assertions.assertThat(e.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");
    }

    @Test
    void login() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberService.join(member);

        Assertions.assertThat(memberService.login("taejin7824@gmail.com", "1234")).isEqualTo(true);

        IllegalStateException e = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.login("taejin7824@gmail.com", "qwer");
        });
        Assertions.assertThat(e.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다. 다시 한번 확인해주세요.");

        e = org.junit.jupiter.api.Assertions.assertThrows(IllegalStateException.class, () -> {
            memberService.login("taejin7824@kakao.com", "qwer");
        });
        Assertions.assertThat(e.getMessage()).isEqualTo("존재하지 않는 이메일입니다. 다시 한번 확인해주세요.");
    }
}