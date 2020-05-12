package me.hjeong._3_scope_of_bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class AppRunner implements ApplicationRunner {
    @Autowired
    ApplicationContext ctx;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Single single = ctx.getBean(Single.class);
        single.setName("single scope bean");

        Proto proto = ctx.getBean(Proto.class);
        proto.setName("proto scope bean");

        System.out.println(ctx.getBean(Single.class).getName());
        System.out.println(ctx.getBean(Single.class).getName());
        System.out.println(ctx.getBean(Single.class).getName() + "\n");

        System.out.println(ctx.getBean(Proto.class).getName());
        System.out.println(ctx.getBean(Proto.class).getName());
        System.out.println(ctx.getBean(Proto.class).getName() + "\n");
    }
}
