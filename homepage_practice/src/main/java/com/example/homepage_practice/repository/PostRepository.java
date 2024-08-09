package com.example.homepage_practice.repository;

import com.example.homepage_practice.domain.Member;
import com.example.homepage_practice.domain.Post;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PostRepository {
    @Autowired
    private final EntityManager em;

    public PostRepository(EntityManager em) {
        this.em = em;
    }

    public Post save(Post post) {
        em.persist(post);

        return post;
    }

    public Optional<Post> findById(Long id) {
        return Optional.ofNullable(em.find(Post.class, id));
    }

    public List<Post> findByTitle(String title) {
        return em.createQuery("SELECT p FROM Post p WHERE p.title = :title", Post.class)
                .setParameter("title", title)
                .getResultList();
    }

    public List<Post> findByMemberId(Member member) {
        return em.createQuery("SELECT p FROM Post p WHERE p.member.id = :memberId", Post.class)
                .setParameter("memberId", member.getId())
                .getResultList();
    }

    public List<Post> findAll() {
        return em.createQuery("SELECT p FROM Post p", Post.class)
                .getResultList();
    }
}
