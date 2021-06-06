package me.hjeong.aws_springboot.web.dto;

import lombok.Getter;
import me.hjeong.aws_springboot.domain.posts.Posts;

@Getter // Setter 는 만들어 놓지 않는다
public class PostsResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    public PostsResponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }

}
