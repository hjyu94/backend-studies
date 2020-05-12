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

        // 매번 같은 이름이 찍힌다
        System.out.println(ctx.getBean(Single.class).getName());
        System.out.println(ctx.getBean(Single.class).getName());
        System.out.println(ctx.getBean(Single.class).getName() + "\n");

        // 매번 null이 찍힌다. (매번 다른 객체라서)
        System.out.println(ctx.getBean(Proto.class).getName());
        System.out.println(ctx.getBean(Proto.class).getName());
        System.out.println(ctx.getBean(Proto.class).getName() + "\n");

        // proto 에서 주입받은 single의 이름은
        // 모두 위와 같은 이름으로 찍힌다.
        // (ctx 안에 single 이 단 하나뿐이므로)
        // -> 문제 없음
        System.out.println(ctx.getBean(Proto.class).getSingle().getName());
        System.out.println(ctx.getBean(Proto.class).getSingle().getName());
        System.out.println(ctx.getBean(Proto.class).getSingle().getName());
        System.out.println("문제 없음!\n");

        // single 에서 주입받은 proto의 이름은
        // proto 처럼 작동하고 있다면 매번 다른 객체이므로 null이 나와야 하나
        // single 이 singleton 이기 때문에 그 안에 단 하나의 proto만 존재한다.
        // -> 매번 같은 객체, 매번 같은 이름이 나옴
        Proto proto_from_single = ctx.getBean(Single.class).getProto();
        proto_from_single.setName("proto from single");
        System.out.println(ctx.getBean(Single.class).getProto().getName());
        System.out.println(ctx.getBean(Single.class).getProto().getName());
        System.out.println(ctx.getBean(Single.class).getProto().getName());
        System.out.println("proto scope 로 작동하지 않는다!\n");

        /*
            [Single에서 빈으로 주입받은 Proto를 Proto scope로 쓰기 위한 해결법]
            1. proxy
            2. ObjectProvider 사용
         */

        // [1] proxy
        // single 빈에서 주입받은 빈이 proto로 작동하고 싶으면
        // proto를 proxy로 감싼 protoUsingProxy 빈을 사용하면 된다.
        ProtoUsingProxy protoUsingProxy = ctx.getBean(Single.class).getProtoUsingProxy();
        proto_from_single.setName("proto from single using proxy");
        System.out.println(ctx.getBean(Single.class).getProtoUsingProxy().getName());
        System.out.println(ctx.getBean(Single.class).getProtoUsingProxy().getName());
        System.out.println(ctx.getBean(Single.class).getProtoUsingProxy().getName() + "\n");

        // [2] ObjectProvider
        Proto protoFromObjectProvider = ctx.getBean(Single.class).getProtoFromObjectProvider();
        protoFromObjectProvider.setName("proto from objectprovider<T>");
        System.out.println(ctx.getBean(Single.class).getProtoFromObjectProvider().getName());
        System.out.println(ctx.getBean(Single.class).getProtoFromObjectProvider().getName());
        System.out.println(ctx.getBean(Single.class).getProtoFromObjectProvider().getName());
    }
}
