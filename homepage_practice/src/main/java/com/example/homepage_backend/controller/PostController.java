package com.example.homepage_backend.controller;

import com.example.homepage_backend.domain.Member;
import com.example.homepage_backend.domain.Post;
import com.example.homepage_backend.domain.dto.request.WritePostRequest;
import com.example.homepage_backend.domain.dto.response.PostResponse;
import com.example.homepage_backend.domain.dto.response.ResponseDTO;
import com.example.homepage_backend.service.MemberService;
import com.example.homepage_backend.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<ResponseDTO<PostResponse>> writePost(@RequestBody WritePostRequest req) {
        try {
            Member member = memberService.getMemberByNickname(req.getNickname());
            PostResponse res = new PostResponse(
                    postService.write(
                            new Post(req.getTitle(), req.getContent(), member)
                    )
            );

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            "게시물이 성공적으로 작성되었습니다.",
                            res
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
    public ResponseEntity<ResponseDTO<List<PostResponse>>> getPostsByNickname(@RequestParam(defaultValue = "") String nickname) {
        try {
            List<Post> posts;
            if (nickname.isBlank()) {
                posts = postService.getAllPosts();
            } else {
                Member member = memberService.getMemberByNickname(nickname);
                posts = postService.getPostsByMember(member);
            }

            List<PostResponse> res = posts.stream().map(PostResponse::new).collect(Collectors.toList());

            return new ResponseEntity<>(
                    new ResponseDTO<>(
                            String.format("조회된 게시물 %d개", res.size()),
                            res
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
