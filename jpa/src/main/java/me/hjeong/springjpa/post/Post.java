package me.hjeong.springjpa.post;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
public class Post extends AbstractAggregateRoot<Post> {

    @Id @GeneratedValue
    private Long id;

    private String title;

    @Lob // 255 자가 넘는 String 의 경우
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date created;

    public Post publish() {
        this.registerEvent(new PostPublishedEvent(this));
        return this;
    }
}
