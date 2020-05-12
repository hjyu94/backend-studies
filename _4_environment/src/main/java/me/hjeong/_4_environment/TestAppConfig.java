package me.hjeong._4_environment;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test") // -> 특정 변수에만 붙일 수도 있다.
// test라는 프로파일로 애플리케이션을 실행 했을때만 해당 빈 설정파일이 적용된다.

public class TestAppConfig {
    @Bean
    public ClassForTest classForTest() {
        return new ClassForTest();
    }
    // test에 필요한 클래스만 빈으로 설정하는 빈 설정파일
}
