package me.hjeong.aws_springboot;

import me.hjeong.aws_springboot.web.HelloController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    테스트를 지행할 때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킵니다
    여기서는 Spring Runner라는 스프링 실행자를 사용합니다
    즉 스프링 부트 테스트와 JUnit 사이에 연결자 역할을 합니다
 */
@RunWith(SpringRunner.class)
/*
    여러 스프링 테스트 어노테이션 중 Web(Spring MVC)에 집중할 수 있는 어노테이션
    @Controller, @ControllerAdvice 등을 사용할 수 있습니다.
    단 @Service, @Component, @Respository 등은 사용할 수 없습니다
    HelloControllerTest 에서는 컨트롤러만 사용하기 때문에 선언합니다

    수동으로 브라우저나 Postman을 켜서 테스트하지 않고 그 전에 테스트 코드를 작성해서 돌려보는 습관이 중요합니다.
 */
@WebMvcTest(controllers = HelloController.class)
public class HelloControllerTest {

    /*
        스프링이 관리하는 빈을 주입 받습니다
     */
    @Autowired
    /*
        웹 API를 테스트할 때 사용합니다.
        스프링 MVC 테스트의 시작점입니다.
        이 클래스를 통해 HTTP GET, POST 등에 대한 API를 테스트 할 수 있습니다.
     */
    private MockMvc mvc;

    @Test
    public void hello가_리턴된다() throws Exception {
        String hello = "hello";

        mvc.perform(get("/hello"))
                .andExpect(status().isOk()) // HTTP Header의 Status를 검증합니다
                .andExpect(content().string(hello)); // 응답 본문의 내용을 검증합니다
    }

    @Test
    public void helloDto가_리턴된다() throws Exception {
        String name = "hello";
        int amount = 1000;

        mvc.perform(
                get("/hello/dto")
                .param("name", name) // 1)
                .param("amount", String.valueOf(amount))
        )
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.name", is(name))) // 2)
                .andExpect(jsonPath("$.amount", is(amount)));
    }

    /*
        1) param
        - API 테스트 할 때 사용될 요청 파라미터를 설정
        - 단 값은 String 만 허용된다.
        - 숫자/날짜 등의 데이터 등록시 문자열로 변경해야 한다.

        2) jsonPath
        - json 응답값을 필드별로 검증할 수 있는 메소드
        - $를 기준으로 필드명을 명시
     */
}
