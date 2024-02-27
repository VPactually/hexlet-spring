package io.hexlet.spring.controllers;

import io.hexlet.spring.datetime.DayTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {

    @Autowired
    private DayTime daytime;

    @GetMapping("/")
    public String home() {
        return "Hello, World!\nIt is " + daytime.getName() + " now";
    }
}
