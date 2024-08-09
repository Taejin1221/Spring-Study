package com.example.homepage_practice.controller;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.domain.Post;
import com.example.homepage_practice.domain.dto.request.WritePostRequest;
import com.example.homepage_practice.domain.dto.response.ResponseDTO;
import com.example.homepage_practice.service.MemberService;
import com.example.homepage_practice.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {
    PostService postService;
    MemberService memberService;

    @Autowired
    public PostController(PostService postService, MemberService memberService) {
        this.postService = postService;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO<Post>> writePost(@RequestBody WritePostRequest request) {
        try {
            Member member = memberService.getMemberByNickname(request.getNickname());
            Post post = postService.write(new Post(request.getTitle(), request.getContent(), member));

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            "게시물이 성공적으로 작성되었습니다.",
                            post
                    ), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            e.getMessage(),
                            null
                    ),HttpStatus.OK);
        }
    }

    @GetMapping
    public ResponseEntity<ResponseDTO<List<Post>>> getPostsByNickname(@RequestParam(defaultValue = "") String nickname) {
        try {
            List<Post> posts;
            if (nickname.isBlank()) {
                posts = postService.getAllPosts();
            } else {
                Member member = memberService.getMemberByNickname(nickname);
                posts = postService.getPostsByMember(member);
            }

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("조회된 게시물 %d개", posts.size()),
                            posts
                    ), HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            e.getMessage(),
                            null
                    ), HttpStatus.OK);
        }
    }
}
