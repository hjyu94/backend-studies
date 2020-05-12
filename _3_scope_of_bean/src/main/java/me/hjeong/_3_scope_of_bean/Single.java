package me.hjeong._3_scope_of_bean;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
class Single {
    @Autowired
    Proto proto;

    @Autowired
    ProtoUsingProxy protoUsingProxy;

    @Autowired
    ObjectProvider<Proto> protoObjectProvider;

    private String name;

    public Proto getProto() {
        return proto;
    }

    public void setProto(Proto proto) {
        this.proto = proto;
    }

    public ProtoUsingProxy getProtoUsingProxy() {
        return protoUsingProxy;
    }

    public void setProtoUsingProxy(ProtoUsingProxy protoUsingProxy) {
        this.protoUsingProxy = protoUsingProxy;
    }

    public Proto getProtoFromObjectProvider() {
        return protoObjectProvider.getIfAvailable();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}