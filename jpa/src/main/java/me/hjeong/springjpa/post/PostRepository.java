package me.hjeong.springjpa.post;

import me.hjeong.springjpa.MyRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends PostCustomRepository<Post>, MyRepository<Post, Long> {

    List<Post> findByTitleStartsWith(String title);

    List<Post> findByTitle(String title);

}
