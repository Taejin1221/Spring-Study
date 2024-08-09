package com.example.homepage_practice.service;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.domain.Post;
import com.example.homepage_practice.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post write(Post post) {
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public List<Post> getPostsByMember(Member member) {
        return postRepository.findByMemberId(member);
    }
}
