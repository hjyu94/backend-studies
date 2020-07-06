package me.hjeong.springjpa;

import me.hjeong.springjpa.post.Post;
import me.hjeong.springjpa.post.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired PostRepository postRepository;

    @Test
    public void crud() {
        // Repository 가 빈으로 잘 등록이 되었나?
        // 등록이 잘 되지 않았다면 테스트가 실패할 것이다.

        // 1. 아무 쿼리도 안 날아간다.
        Post post = new Post();
        post.setTitle("hibernate");
        postRepository.save(post);
        postRepository.delete(post);
    }

    @Test
    public void crud_2() {
        Post post = new Post();
        post.setTitle("hibernate");
        postRepository.save(post); // insert 쿼리 날아감
        postRepository.findMyPost(); // select 쿼리 날아감
        postRepository.delete(post); // 쿼리 날아가지 않음 (어차피 테스트 코드는 롤백 트랜잭션이라서)
    }

    @Test
    public void crud_3() {
        Post post = new Post();
        post.setTitle("hibernate");
        postRepository.save(post);
        postRepository.findMyPost();
        postRepository.delete(post); // flush() 가 호출되므로 delete 쿼리 날아간다
        postRepository.flush();
    }
}