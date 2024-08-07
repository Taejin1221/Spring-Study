package com.example.homepage_practice.service;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.repository.MemberRepository;
import jakarta.persistence.NoResultException;
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
        try {
            memberRepository.findByEmail(email);

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    private boolean isDuplicatedNickname(String nickname) {
        try {
            memberRepository.findByNickname(nickname);

            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    public boolean login(String email, String password) {
        try {
            String correctPassword = memberRepository.findByEmail(email).getPassword();
            if (correctPassword.equals(password)) {
                return true;
            } else {
                throw new IllegalStateException("비밀번호가 일치하지 않습니다. 다시 한번 확인해주세요.");
            }
        } catch (NoResultException e) {
            throw new IllegalStateException("존재하지 않는 이메일입니다. 다시 한번 확인해주세요.");
        }
    }
}
