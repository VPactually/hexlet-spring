package io.hexlet.spring.datetime;

import jakarta.annotation.PostConstruct;

public class Night implements DayTime {

    private String name = "night";

    @Override
    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean " + name + " is initialized");
    }
}
