package com.example.homepage_practice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequest {
    private String email;
    private String password;
    private String nickname;
}
