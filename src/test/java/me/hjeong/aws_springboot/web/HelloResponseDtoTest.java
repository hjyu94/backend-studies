package me.hjeong.aws_springboot.web;

import me.hjeong.aws_springboot.web.dto.HelloResponseDto;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트() {
       // given
       String name = "test";
       int amount = 1000;

       // when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        // then
        assertThat(dto.getName()).isEqualTo(name); // 1, 2
        assertThat(dto.getAmount()).isEqualTo(amount);
    }
}

/*
    1) assertThat
    - assertj 테스트 검증 라이브러리의 검증 메소드
    - 검증하고 싶은 대상을 메소드 인자로 받는다

    2) isEqualTo
    - assertj의 동등 비교 메소드
    - assertThat에 있는 값과 isEqualTo의 값을 비교해서 같을 때만 성공합니다.

    *) Junit 과 비교 시 asssertj의 장
    - assertj 역시 Junit에서 자동으로 라이브러리 등록을 해준다
    - CoreMatchers와 달리 추가적으로 라이브러리가 필요하지 않는다
        - Junit의 assertThat을 쓰게 되면 is()와 같이 CoreMathers 라이브러리가 필요합니다
    - 자동완성이 좀 더 확실하게 지원됩니다
 */