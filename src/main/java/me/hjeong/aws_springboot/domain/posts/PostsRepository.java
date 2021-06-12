package me.hjeong.aws_springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts p ORDER BY p.id DESC")
    List<Posts> findAllDesc(); // 1

}

/*
    - 보통 ibatis 나 MyBatis 등에서 Dao 라고 불리는 DB Layer 접근자
    - @Repository 불필요

    - (참고) 규모가 있는 프로젝트에서의 데이터 조회는 FK의 조인, 복잡한 조건 등으로 인해
      이런 Entity 클래스 만으론 처리하기 어려워 조회용 프레임워크를 추가로 사용합니다
      대표적으로 querydsl, jooq, MyBatis 등이 있습니다.
 */