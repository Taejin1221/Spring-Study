package com.example.homepage_practice.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class WritePostRequest {
    private String nickname;
    private String title;
    private String content;
}
