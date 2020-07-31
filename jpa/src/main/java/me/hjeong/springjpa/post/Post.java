package me.hjeong.springjpa.post;

import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
@NamedQuery(
        name = "Post.findByTitle"
        , query = "SELECT p FROM Post AS p WHERE p.title = ?1" // JPQL
)
public class Post extends AbstractAggregateRoot<Post> {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @Lob // 255 자가 넘는 String 의 경우
    private String content;

    private LocalDateTime created;

    public Post publish() {
        this.registerEvent(new PostPublishedEvent(this));
        return this;
    }
}
