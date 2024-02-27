package io.hexlet.spring;

import io.hexlet.spring.datetime.DayTime;
import io.hexlet.spring.datetime.Day;
import io.hexlet.spring.datetime.Night;
import net.datafaker.Faker;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;


@SpringBootApplication
@RestController
@EnableJpaAuditing
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @RequestScope
    public DayTime getDayTime() {
        var now = LocalDateTime.now();
        return now.getHour() >= 6 && now.getHour() < 22 ? new Day() : new Night();
    }

    @Bean
    public Faker getFaker() {
        return new Faker();
    }

}
