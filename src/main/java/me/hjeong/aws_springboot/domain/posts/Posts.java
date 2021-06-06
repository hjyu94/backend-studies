package me.hjeong.aws_springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.hjeong.aws_springboot.domain.BaseTimeEntity;

import javax.persistence.*;

@Getter // 6
@NoArgsConstructor // 5
@Entity // 1
public class Posts extends BaseTimeEntity {

    @Id // 2
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 3
    private Long id;

    @Column(length = 500, nullable = false) // 4
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder // 7
    public Posts(Long id, String title, String content, String author) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

}

/*
    1) @Entity
    - 테이블과 링크될 클래스
    - 클래스의 카멜케이스 이름 -> 언더스코어 네이밍으로 테이블 이름에 매칭

    2) @Id
    - 해당 테이블의 PK 필드

    3) @GeneratedValue
    - PK의 생성 규칙을 나타냄
    - strategy = GenerationType.IDENTITY 옵션을 추가해야만 auto_increment가 된다
    - 웬만하면 Long 타입의 auto_increment 를 추천
    - 주민등록번호와 같이 비즈니스상의 유니크 키나, 여러 키를 조합한 복합키를 PK로 잡으면 난감할때가 종종 있다.
        - FK를 맺을 때 다른 테이블에서 복합키를 전부 갖고 있거나, 중간 테이블이 필요해질 수 있다.
        - 인덱스에 좋지 못한 영향을 끼친다
        - 유니크한 조건이 변경될 경우 PK 전체를 수정해야 하는 일이 발생

    4) @Column
    - 테이블의 칼럼, 굳이 선언하지 않더라도 클래스 필드는 모두 칼럼이 된다.
    - 기본 값 외에 추가로 변경이 필요한 옵션에 사용
    - 문자열인 경우 VARCHAR(255)가 기본값인데 사이즈를 500으로 늘리고 싶다는 등

    5) @NoArgsConstructor
    - 기본 생성자 자동 추가

    6) @Getter
    - 클래스 내 모든 필드의 Getter 메소드 생성

    7) @Builder
    - 해당 클래스의 빌더 패턴 클래스 생성
    - 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함

    *) Setter 메소드를 Entity 클래스에서 절대 만들지 않는다
    - 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가한다
        ex) order.setStatus(false) 가 아니라 order.cancelOrder() 에서 값 변경
    - 생성자를 통해 값을 채운 후 DB 에 insert 하거나, 해당 이벤트에 맞는 public 메소드를 호출하여 변경하는 것이 전제
 */
