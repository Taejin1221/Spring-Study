package com.example.homepage_practice.service;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.domain.Post;
import com.example.homepage_practice.repository.MemberRepository;
import com.example.homepage_practice.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
class PostServiceTest {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Test
    void write() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Post post1 = new Post("Post1", "Hello, world!", member1);
        postService.write(post1);

        List<Post> result = postRepository.findByTitle("Post1");
        Assertions.assertThat(result).extracting(Post::getTitle).containsExactly(post1.getTitle());
    }

    @Test
    void getAllPosts() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Member member2 = new Member("taejin7824@kakao.com", "qwer", "gene");
        memberRepository.save(member2);

        Post post1 = new Post("Post1", "Hello, world!", member1);
        postRepository.save(post1);

        Post post2 = new Post("Post2", "Hello, taejin!", member1);
        postRepository.save(post2);

        Post post3 = new Post("Post3", "Hello, gene!", member2);
        postRepository.save(post3);

        List<Post> results = postService.getAllPosts();
        Assertions.assertThat(results.size()).isEqualTo(3);
        Assertions.assertThat(results).extracting(Post::getTitle).containsExactly(post1.getTitle(), post2.getTitle(), post3.getTitle());
    }

    @Test
    void getPostsByMember() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Post post1 = new Post("Post1", "Hello, world!", member1);
        postRepository.save(post1);

        Post post2 = new Post("Post1", "Hello, taejin!", member1);
        postRepository.save(post2);

        List<Post> results = postService.getPostsByMember(member1);
        Assertions.assertThat(results.size()).isEqualTo(2);
        Assertions.assertThat(results).extracting(Post::getTitle).containsExactly(post1.getTitle(), post1.getTitle());
    }
}