package me.hjeong._1_ioc_container_and_bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 빈 설정 파일. 이 안에서 빈 명세를 작성한다.
public class AppConfig {

    @Bean
    public BookRepository bookRepository() {
        return new BookRepository();
    }

    @Bean
    public BookService bookService() {
        return new BookService();
    }
}
