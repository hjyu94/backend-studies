package me.hjeong.springjpa;

import me.hjeong.springjpa.post.Post;
import me.hjeong.springjpa.post.PostPublishedEvent;
import me.hjeong.springjpa.post.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PostRepositoryTestConfig.class) // 해당 설정파일을 Import 한다 (그 안에 정의된 빈들이 만들어진다.)
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired ApplicationContext applicationContext;

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

    @Test
    public void customizing_base_repository() {
        Post post = new Post();
        post.setTitle("hibernate");

        assertThat(postRepository.contains(post)).isFalse(); // transient state

        postRepository.save(post);

        assertThat(postRepository.contains(post)).isTrue(); // persistent state
    }

    @Test
    @DisplayName("이벤트 발생시키고 이를 처리하는 핸들러 테스트")
    public void event() {
        Post post = new Post();
        post.setTitle("event");
        post.publish();
        postRepository.save(post); // save() 될 때 도메인에 쌓여있던 이벤트가 모두 발생된다.
        postRepository.findMyPost(); // 롤백 되어서 insert 쿼리가 발생하지 않기 때문에 추가한 select 문
    }
}