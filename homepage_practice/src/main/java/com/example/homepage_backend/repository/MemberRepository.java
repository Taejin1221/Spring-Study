package com.example.homepage_backend.repository;

import com.example.homepage_backend.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MemberRepository {
    @Autowired
    private final EntityManager em;

    public MemberRepository(EntityManager em) {
        this.em = em;
    }

    public Member save(Member member) {
        em.persist(member);

        return member;
    }

    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(em.find(Member.class, id));
    }

    public List<Member> findAll() {
        return em.createQuery("SELECT m FROM Member m", Member.class)
                .getResultList();
    }

    public Optional<Member> findByEmail(String email) {
        try {
            return Optional.of(em.createQuery("SELECT m FROM Member m WHERE m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByNickname(String nickname) throws NoResultException {
        try {
            return Optional.of(em.createQuery("SELECT m FROM Member m WHERE m.nickname = :nickname", Member.class)
                    .setParameter("nickname", nickname)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
