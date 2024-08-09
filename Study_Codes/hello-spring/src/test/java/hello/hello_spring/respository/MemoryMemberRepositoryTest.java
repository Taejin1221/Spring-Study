package hello.hello_spring.respository;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemoryMemberRepositoryTest {

    MemoryMemberRepository repository = new MemoryMemberRepository();

    @AfterEach
    public void afterEach() {
        repository.clearStore();
    }

    @Test
    public void save() {
        Member member = new Member();
        member.setName("taejin");

        repository.save(member);

        Member result = repository.findById(member.getId()).get();
        assertThat(member).isEqualTo(result);
    }

    @Test
    public void findByname() {
        Member member1 = new Member();
        member1.setName("taejin1");
        repository.save(member1);

        Member member2 = new Member();
        member2.setName("taejin2");
        repository.save(member2);

        Member result = repository.findByName("taejin1").get();

        assertThat(result).isEqualTo(member1);
//        assertThat(result).isEqualTo(member2);
    }

    @Test
    public void findAll() {
        Member member1 = new Member();
        member1.setName("taejin1");
        repository.save(member1);
        
        Member member2 = new Member();
        member2.setName("taejin2");
        repository.save(member2);

        List<Member> result = repository.findAll();

        assertThat(result.size()).isEqualTo(2);
    }
}
