package me.hjeong._3_scope_of_bean;

import org.springframework.stereotype.Component;

@Component
class Single {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}