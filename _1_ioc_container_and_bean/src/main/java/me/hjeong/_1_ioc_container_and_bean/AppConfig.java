package me.hjeong._1_ioc_container_and_bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 빈 설정 파일. 이 안에서 빈 명세를 작성한다.
public class AppConfig {

    @Bean
    public BookRepository bookRepository() {
        return new BookRepository();
    }

    // 의존성 주입하기[1] setter
    @Bean
    public BookService bookService() {
        BookService bookService = new BookService();
        bookService.setBookRepository(bookRepository());
        return new BookService();
    }

    // 의존성 주입하기[2] argument
    @Bean
    public BookService bookService2(BookRepository bookRepository) {
        BookService bookService = new BookService();
        bookService.setBookRepository(bookRepository);
        return new BookService();
    }
}
// [org.springframework.context.annotation.internalConfigurationAnnotationProcessor
// , org.springframework.context.annotation.internalAutowiredAnnotationProcessor
// , org.springframework.context.annotation.internalCommonAnnotationProcessor
// , org.springframework.context.event.internalEventListenerProcessor
// , org.springframework.context.event.internalEventListenerFactory
// , appConfig, bookRepository, bookService, bookService2]

// @Configuration 을 붙이면 기본적인 빈들과 우리가 만든 함수명을 빈 이름으로 한 빈이 만들어진다.
// @Bean 을 이용해서 직접 빈을 만들 때는 setter, argument 로 의존성을 주입할 수 있다.