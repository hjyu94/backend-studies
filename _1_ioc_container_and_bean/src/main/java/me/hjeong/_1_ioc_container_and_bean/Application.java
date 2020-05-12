package me.hjeong._1_ioc_container_and_bean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");

        String[] beanDefinitionNames = context.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beanDefinitionNames));

        BookService bookService = (BookService) context.getBean("bookService");
        System.out.println(bookService.bookRepository != null);
    }

    // application.xml 을 빈 설정 파일로 사용한 스프링 IoC 컨테이너를 만듦

    // ApplicationContext.getBeanDefinitionNames();
    // ApplicationContext.getBean(String beanName);

    // bookRepository 객체를 따로 생성하지 않아도
    // 스프링 IoC 컨테이너가 객체를 생성하고(그 객체를 빈이라고 한다) 컨테이너 안에 가지고 있다가
    // 다른 빈(bookService)에서 빈(bookRepository)으로 주입받아 사용할 수 있다.
}
