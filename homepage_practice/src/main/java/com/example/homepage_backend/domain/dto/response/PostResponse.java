package com.example.homepage_backend.domain.dto.response;

import com.example.homepage_backend.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostResponse {
    private String nickname;
    private String title;
    private String content;

    public PostResponse(Post post) {
        this.nickname = post.getMember().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
