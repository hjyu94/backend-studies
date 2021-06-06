package me.hjeong.aws_springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Posts, Long> {
}

/*
    - 보통 ibatis 나 MyBatis 등에서 Dao 라고 불리는 DB Layer 접근자
    - @Repository 불필요
 */