package me.hjeong.springjpa;

import me.hjeong.springjpa.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PostRepositoryTest {

    @Autowired PostRepository postRepository;

    @Test
    public void crud() {
        // Repository 가 빈으로 잘 등록이 되었나?
        // 등록이 잘 되지 않았다면 테스트가 실패할 것이다.
    }

}