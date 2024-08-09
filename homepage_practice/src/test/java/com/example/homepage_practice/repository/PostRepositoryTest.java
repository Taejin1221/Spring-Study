package com.example.homepage_practice.repository;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.domain.Post;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@Transactional
class PostRepositoryTest {
    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void findById() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member);

        Post post = new Post("Post1", "Hello, world!", member);
        postRepository.save(post);

        Optional<Post> result = postRepository.findById(Integer.toUnsignedLong(1));
        Assertions.assertThat(post).isEqualTo(result.get());

        result = postRepository.findById(Integer.toUnsignedLong(2));
        Assertions.assertThat(result).isEqualTo(Optional.empty());
    }

    @Test
    void findByTitle() {
        Member member = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member);

        Post post1 = new Post("Post1", "Hello, world!", member);
        postRepository.save(post1);

        Post post2 = new Post("Post1", "Hello, taejin!", member);
        postRepository.save(post2);

        List<Post> result = postRepository.findByTitle("Post1");
        Assertions.assertThat(result.size()).isEqualTo(2);
        Assertions.assertThat(result).extracting(Post::getTitle).containsExactly("Post1", "Post1");

        result = postRepository.findByTitle("Post2");
        Assertions.assertThat(result.isEmpty()).isEqualTo(true);
    }

    @Test
    void findByMemberId() {
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

        List<Post> results = postRepository.findByMemberId(member1);
        Assertions.assertThat(results.size()).isEqualTo(2);
        Assertions.assertThat(results).extracting(Post::getTitle).containsExactly("Post1", "Post2");
    }

    @Test
    void findAll() {
        Member member1 = new Member("taejin7824@gmail.com", "1234", "taejin");
        memberRepository.save(member1);

        Member member2 = new Member("taejin7824@kakao.com", "qwer", "gene");
        memberRepository.save(member2);

        Post post1 = new Post("Post1", "Hello, world!", member1);
        postRepository.save(post1);

        Post post2 = new Post("Post3", "Hello, taejin!", member1);
        postRepository.save(post2);

        Post post3 = new Post("Post2", "Hello, gene!", member2);
        postRepository.save(post3);

        List<Post> results = postRepository.findAll();
        Assertions.assertThat(results.size()).isEqualTo(3);
        Assertions.assertThat(results).extracting(Post::getTitle).containsExactlyInAnyOrder("Post1", "Post2", "Post3");
    }
}