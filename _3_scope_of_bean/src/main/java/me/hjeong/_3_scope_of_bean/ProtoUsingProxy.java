package me.hjeong._3_scope_of_bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
// ScopedProxyMode.TARGET_CLASS: dynamic proxy 를 사용하겠다는 뜻.
// ScopedProxyMode.DEFAULT: 기본값으로 dynaic proxy 를 사용하지 않는다.

public class ProtoUsingProxy {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/*
    proxy 모드를 설정한다?
    다른 빈들이 해당 빈을 주입받아 사용할 때 클래스 기반의 proxy로 빈을 감싸고
    감싼 Proxy을 다른 빈들에게 주입하여 사용하게 해라

    만약 Single에서 ProtoUsingProxy를 주입받아 사용한다면
    ProtoUsingProxy를 상속받은 Proxy를 만들고
    해당 Proxy가 빈이 되어 Single에 주입된다.

    싱글이 프로토타입의 빈을 직접 참조하면 안되기 때문.
    직접 쓰면 프로토 타입을 매번 새로운 인스턴스로 바꿔줘야 하는데 바꿔줄 수가 없기 때문이다.
 */