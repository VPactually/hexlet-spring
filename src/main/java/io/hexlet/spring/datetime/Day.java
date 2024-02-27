package io.hexlet.spring.datetime;

import jakarta.annotation.PostConstruct;

public class Day implements DayTime {
    private String name = "day";

    public String getName() {
        return name;
    }

    @PostConstruct
    public void init() {
        System.out.println("Bean " + name + " is initialized");
    }

}
