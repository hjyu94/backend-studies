package me.hjeong._11_aop_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        /*
            SpringApplication.run(Application.class, args);
         */

        // 웹 서버를 구동하지 않고 빠르게 실행하는 법
        SpringApplication app = new SpringApplication(Application.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

}
