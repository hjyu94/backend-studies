package me.hjeong.aws_springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

// 스프링 부트 자동 설정, 스프링 Bean 읽기와 생성 모두 자동으로 설정
// 해당 클래스가 있는 곳이 Scan시의 Base package가 되기 때문에 프로젝트 최상단에 존재해야 한다
@SpringBootApplication
// JPA Auditing 활성화
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        // 내장 WAS 실행 (별도로 외부에 WAS를 두지 않고 애플리케이션 실행할 때 내부에서 WAS 실행
        // 서버에 톰캣을 설치할 필요가 없이, 스프링 부트로 만들어진 Jar로 실행 가능하게 된다.
        // 내장 WAS 를 사용해야 언제 어디서나 같은 환경에서 스프링 부트를 배포할 수 있게 된다.
        // 외장 WAS 를 쓰게 되면 모든 서버는 WAS의 종류, 버전, 설정을 일치시켜야 하고
        // 새로운 서버가 추가되면 모든 서버가 같은 WAS 환경을 구축해야만 한다
        // 대표적인 WAS 중 하나인 톰캣 역시 서블릿으로 이루어진 자바 애플리케이션.
        // -> 똑같은 코드를 사용하므로 성능상 이슈는 크게 고려하지 않아도 된다.
        SpringApplication.run(Application.class, args);
    }

}
