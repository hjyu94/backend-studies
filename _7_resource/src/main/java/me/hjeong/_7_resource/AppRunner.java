package me.hjeong._7_resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ResourceLoader resourceLoader;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println(resourceLoader.getClass()); // AnnotationConfigApplicationContext

        Resource resource = resourceLoader.getResource("text.txt");
        System.out.println(resource.getClass());

        resource = resourceLoader.getResource("classpath:text.txt");
        System.out.println(resource.getClass());

        resource = resourceLoader.getResource("file:///text.txt");
        System.out.println(resource.getClass());

        new ClassPathXmlApplicationContext("config.xml"); // class 경로에서 찾는다
        new FileSystemXmlApplicationContext("config.xml"); // file 경로에서 찾는다
        new WebApplicationContext("config.xml"); // servlet context 경로에서 찾는다
    }
}
