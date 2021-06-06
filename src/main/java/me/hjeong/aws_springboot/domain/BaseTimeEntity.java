package me.hjeong.aws_springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass // 1
@EntityListeners(AuditingEntityListener.class) // 2
public class BaseTimeEntity {

    @CreatedDate // 3
    private LocalDateTime createdDate;

    @LastModifiedDate // 4
    private LocalDateTime modifiedDate;

}

/*
    1) @MappedSuperclass
    - JPA Entity 들이 BaseTimeEntity 를 상속하는 경우 해당 클래스의 필드들도 칼럼으로 인식하도록 한다

    2) @EntityListeners(AuditingEntityListener.class)
    - BaseTimeEntity 클래스에 Auditing 기능을 포함시킨다

    3) @CreatedDate
    - Entity 가 생성되어 저장될 때 시간이 자동 저장됨

    4) @LastModifiedDate
    - 조회한 Entity의 값을 변경할 때 시간이 자동 저장됨
 */