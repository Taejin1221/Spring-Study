package com.example.homepage_backend.repository;

import com.example.homepage_backend.domain.Member;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired MemberRepository memberRepository;

    @Test
    void save() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Member result = memberRepository.findByEmail("taejin7824@gmail.com").get();
        assertThat(result).isEqualTo(member1);
    }

    @Test
    void findById() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Optional<Member> result = memberRepository.findById(member1.getId());
        assertThat(result.get()).isEqualTo(member1);

        result = memberRepository.findById(Integer.toUnsignedLong(123));
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void findAll() {
        List<Member> results = memberRepository.findAll();
        assertThat(results.isEmpty()).isEqualTo(true);

        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Member member2 = new Member("taejin7824@kakao.com", "qwer", "gene");
        memberRepository.save(member2);

        results = memberRepository.findAll();
        assertThat(results.size()).isEqualTo(2);
        assertThat(results).extracting(Member::getEmail).containsExactly(member1.getEmail(), member2.getEmail());
    }

    @Test
    void findByEmail() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Optional<Member> result = memberRepository.findByEmail("taejin7824@gmail.com");
        assertThat(result.get()).isEqualTo(member1);

        result = memberRepository.findByEmail("taejin7824@kakao.com");
        assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void findByNickname() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Optional<Member> result = memberRepository.findByNickname("taejin");
        assertThat(result.get()).isEqualTo(member1);

        result = memberRepository.findByNickname("gene");
        assertThat(result).isEqualTo(Optional.empty());
    }
}