package com.example.homepage_practice.repository;

import com.example.homepage_practice.domain.Member;
import jakarta.persistence.NoResultException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    void save() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member);

        Member result = memberRepository.findByEmail("taejin7824@gmail.com");
        Assertions.assertThat(member).isEqualTo(result);
    }

    @Test
    void findById() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member);

        Member result = memberRepository.findById(member.getId());
        Assertions.assertThat(member).isEqualTo(result);

        result = memberRepository.findById(Integer.toUnsignedLong(123));
        Assertions.assertThat(result).isEqualTo(null);
    }

    @Test
    void findAll() {
        NoResultException e = org.junit.jupiter.api.Assertions.assertThrows(NoResultException.class, () -> {
            memberRepository.findAll();
        });
        System.out.println(e.getMessage());

        Member member1 = new Member();
        member1.setEmail("taejin7824@gmail.com");
        member1.setPassword("1234");
        member1.setNickname("taejin");
        memberRepository.save(member1);

        Member member2 = new Member();
        member2.setEmail("taejin7824@kakao.com");
        member2.setPassword("qwer");
        member2.setNickname("gene");
        memberRepository.save(member2);

        List<Member> results = memberRepository.findAll();
        Assertions.assertThat(results.size()).isEqualTo(2);

    }

    @Test
    void findByEmail() {
        NoResultException e = org.junit.jupiter.api.Assertions.assertThrows(NoResultException.class, () -> {
            memberRepository.findByEmail("taejin7824@gmail.com");
        });

        System.out.println(e.getMessage());

        Member member1 = new Member();
        member1.setEmail("taejin7824@gmail.com");
        member1.setPassword("1234");
        member1.setNickname("taejin");
        memberRepository.save(member1);

        Member result = memberRepository.findByEmail("taejin7824@gmail.com");
        Assertions.assertThat(member1).isEqualTo(result);
    }

    @Test
    void findByNickname() {
        NoResultException e = org.junit.jupiter.api.Assertions.assertThrows(NoResultException.class, () -> {
            memberRepository.findByNickname("taejin");
        });

        System.out.println(e.getMessage());

        Member member1 = new Member();
        member1.setEmail("taejin7824@gmail.com");
        member1.setPassword("1234");
        member1.setNickname("taejin");
        memberRepository.save(member1);

        Member result = memberRepository.findByNickname("taejin");
        Assertions.assertThat(member1).isEqualTo(result);
    }
}