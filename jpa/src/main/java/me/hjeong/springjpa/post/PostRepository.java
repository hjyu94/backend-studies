package me.hjeong.springjpa.post;

import me.hjeong.springjpa.MyRepository;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends PostCustomRepository<Post>, MyRepository<Post, Long> {

    List<Post> findByTitleStartsWith(String title);

    List<Post> findByTitle(String title);

    @Query(value = "SELECT p, p.title AS pTitle FROM #{#entityName} AS p WHERE p.content = :content")
    List<Post> findByContent(@Param("content") String keyword, Sort sort);

    @Modifying // (clearAutomatically = true)
    @Query("UPDATE Post p SET p.title = ?1 WHERE p.id = ?2")
    int updateTitle(String title, Long id);
}
