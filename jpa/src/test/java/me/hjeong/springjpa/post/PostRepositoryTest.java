package me.hjeong.springjpa.post;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.JpaSort;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(PostRepositoryTestConfig.class) // 해당 설정파일을 Import 한다 (그 안에 정의된 빈들이 만들어진다.)
class PostRepositoryTest {

    @Autowired PostRepository postRepository;
    @Autowired ApplicationContext applicationContext;
    @PersistenceContext EntityManager entityManager;

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

    @Test
    @DisplayName("save() 기능 1. insert 2. update")
    public void testSaveFunction() {
        Post post = Post.builder()
                .title("jpa")
                .build();
        Post savedPost = postRepository.save(post);
        // post 의 id가 없기 때문에 Transient 상태라고 판단한다
        // 따라서 save() 시에 EntityManager.persist() 가 실행된다.
        // 여기서는 insert 가 일어나지만

        assertThat(entityManager.contains(post)).isTrue(); // 영속화 되어 있는가?
        assertThat(entityManager.contains(savedPost)).isTrue();
        // 또한 save() 결과값으로 항상 영속화 된 상태의 객체를 리턴한다.
        assertThat(savedPost == post);

        Post postUpdate = Post.builder()
                .id(post.getId())
                .title("hibernate")
                .build();
        Post savedPostUpdate = postRepository.save(postUpdate);
        // postUpdate 는 id 값이 있기 때문에 Detached 상태라고 판단한다.
        // 따라서 save() 시에 EntityManager.merge() 가 실행된다.
        // 여기서는 update 가 일어난다.

        assertThat(entityManager.contains(postUpdate)).isFalse();
        assertThat(entityManager.contains(savedPostUpdate)).isTrue();
        assertThat(savedPostUpdate == postUpdate).isFalse();

        postUpdate.setTitle("changed title");
        // postUpdate는 persistent 상태가 아니라서 상태 변화를 감지하지 못한다.
        // 따라서 저장 후에는 반환하는 리턴 값을 사용해서 상태를 변화시켜야 한다
        // savedPostUpdate.setTitle("changed title"); (O)

        List<Post> all = postRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
    }
    // persistent 상태의 객체를 사용하면 객체 상태 변화를 추적하고, 객체 변화 상태가 필요한 경우에 반영이 된다.
    // select 해서 확인 할 때 이전의 상태 변화 들이 update 쿼리로 날아감.
    // 객체를 가지고 오려 그러네? 오 빨리 싱크해야 겠다. 그래야 최신 상태의 데이터를 제대로 가져 올 수 있으니까!

    @Test
    @DisplayName("쿼리 생성 테스트 StartsWith")
    public void findByTitleStartsWith() {
        Post post = Post.builder()
                .title("Spring Data Jpa")
                .build();
        postRepository.save(post);

        List<Post> all = postRepository.findByTitleStartsWith("Spr");
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿼리 찾기 테스트 @NamedQuery")
    public void findByNamedQueryAnnotaion() {
        Post post = Post.builder()
                .title("Spring")
                .build();
        postRepository.save(post);

        List<Post> all = postRepository.findByTitle("Spring");
        assertThat(all.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("쿼리 찾기 테스트 @Query")
    public void findByQueryAnnotation() {
        String content = "This is content";

        Post post = Post.builder()
                .title("Spring")
                .content(content)
                .build();
        postRepository.save(post);

        // PROPERTY
        List<Post> all = postRepository.findByContent(content, Sort.by("title"));
        assertThat(all.size()).isEqualTo(1);

        // ALIAS
        List<Post> all2 = postRepository.findByContent(content, Sort.by("pTitle"));
        assertThat(all2.size()).isEqualTo(1);

    /*
        // ERROR
        // org.springframework.dao.InvalidDataAccessApiUsageException
        // : Sort expression 'LENGTH(title): ASC' must only contain property references or aliases used in the select clause.
        // If you really want to use something other than that for sorting, please use JpaSort.unsafe(…)!
        List<Post> all3 = postRepository.findByContent(content, Sort.by("LENGTH(title)"));
        assertThat(all3.size()).isEqualTo(1);
    */

        // SOLUTION
        List<Post> all4 = postRepository.findByContent(content, JpaSort.unsafe("LENGTH(title)"));
        assertThat(all4.size()).isEqualTo(1);
    }
}