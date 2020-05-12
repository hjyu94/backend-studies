package me.hjeong._1_ioc_container_and_bean2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// [4] @SpringBootAppliction
// 빈 설정파일을 따로 만들 필요 없이
// 자동으로 만들어지는 Application.java 에 @SpringBootApplication 이 붙어 있으면
// Application.java 자체가 빈 설정파일이 되ㅁ녀서
// 해당 패키지 내에서 @Component 가 붙은 클래스는 모두 빈으로 만들고 의존성을 추가해준다.