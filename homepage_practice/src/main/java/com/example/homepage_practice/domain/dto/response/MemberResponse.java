package com.example.homepage_practice.domain.dto.response;

import com.example.homepage_practice.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MemberResponse {
    private String email;
    private String nickname;

    public MemberResponse(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
    }
}
