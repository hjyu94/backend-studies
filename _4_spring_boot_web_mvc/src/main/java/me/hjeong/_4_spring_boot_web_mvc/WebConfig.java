package me.hjeong._4_spring_boot_web_mvc;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

// [1] application.properties

// [2] 스프링 부트의 스프링 MVC 자동 설정 + 추가 설정
@Configuration
public class WebConfig implements WebMvcConfigurer {
}

// [3] 스프링 부트의 스프링 MVC 자동 설정 사용하지 않고 커스텀하기
//@Configuration
//@EnableWebMvc
//public class WebConfig implements WebMvcConfigurer {
//
//}
