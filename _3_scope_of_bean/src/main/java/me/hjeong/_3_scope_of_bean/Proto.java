package me.hjeong._3_scope_of_bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = "prototype")
public class Proto {
    @Autowired
    Single single;

    private String name;

    public Single getSingle() {
        return single;
    }

    public void setSingle(Single single) {
        this.single = single;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
