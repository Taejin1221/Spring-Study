package com.example.homepage_backend.service;

import com.example.homepage_backend.domain.Member;
import com.example.homepage_backend.repository.MemberRepository;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertThat(result).isEqualTo(member);

        Member dupEmail = new Member("taejin7824@gmail.com", "qwer", "gene");
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.join(dupEmail);
        });
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 이메일입니다.");

        Member dupNickname = new Member("taejin7824@kakao.com", "qwer", "taejin");
        e = assertThrows(IllegalStateException.class, () -> {
            memberService.join(dupNickname);
        });
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 닉네임입니다.");
    }

    @Test
    void login() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberService.join(member);

        assertThat(memberService.login("taejin7824@gmail.com", "1234")).isEqualTo(true);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.login("taejin7824@gmail.com", "qwer");
        });
        assertThat(e.getMessage()).isEqualTo("비밀번호가 일치하지 않습니다. 다시 한번 확인해주세요.");

        e = assertThrows(IllegalStateException.class, () -> {
            memberService.login("taejin7824@kakao.com", "qwer");
        });
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 이메일입니다. 다시 한번 확인해주세요.");
    }

    @Test
    void getMemberByNickname() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberService.join(member);

        Member result = memberService.getMemberByNickname("taejin");
        assertThat(result).isEqualTo(member);

        IllegalStateException e = assertThrows(IllegalStateException.class, () -> {
            memberService.getMemberByNickname("gene");
        });
        assertThat(e.getMessage()).isEqualTo("존재하지 않는 닉네임입니다. 다시 한번 확인해주세요.");
    }
}