package com.example.homepage_backend.service;

import com.example.homepage_backend.domain.Member;
import com.example.homepage_backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member join(Member member) {
        if (isDuplicatedEmail(member.getEmail())) {
            throw new IllegalStateException("이미 존재하는 이메일입니다.");
        }

        if (isDuplicatedNickname(member.getNickname())) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }

        return memberRepository.save(member);
    }

    private boolean isDuplicatedEmail(String email) {
        return memberRepository.findByEmail(email).isPresent();
    }

    private boolean isDuplicatedNickname(String nickname) {
        return memberRepository.findByNickname(nickname).isPresent();
    }

    public Member login(String email, String password) {
        Member member = getMemberByEmail(email);

        if (member.getPassword().equals(password)) {
            return member;
        } else {
            throw new IllegalStateException("비밀번호가 일치하지 않습니다. 다시 한번 확인해주세요.");
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() ->
            new IllegalStateException("존재하지 않는 이메일입니다. 다시 한번 확인해주세요.")
        );
    }

    public Member getMemberByNickname(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow(() ->
                new IllegalStateException("존재하지 않는 닉네임입니다. 다시 한번 확인해주세요.")
        );
    }
}
