package me.hjeong.aws_springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter // 1
@RequiredArgsConstructor // 2
public class HelloResponseDto {

    private final String name;
    private final int amount;

}

/*
    1. @Getter
    - 선언된 모든 필드의 get 메소드를 생성해줍니다.

    2. @RequiredArgsConstructor
    - 기본 생성자는 사라지고 선언된 모든 final 필드가 포함된 생성자를 생성해 줍니다.
    - final이 없는 필드는 생성자에 포함되지 않습니다.
    - 최신 버전의 gradle 의 경우 잘 동작하지 않을 수 있다.
    - gradle 다운그레이드 명령어
        - $ ./gradlew wrapper --gradle-version 4.10.2
 */