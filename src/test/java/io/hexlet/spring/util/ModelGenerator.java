package io.hexlet.spring.util;


import io.hexlet.spring.model.Page;
import io.hexlet.spring.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Getter
@Component
public class ModelGenerator {
    private Model<Page> pageModel;
    private Model<User> userModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPages))
                .supply(Select.field(User::getName), () -> faker.name().fullName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .toModel();


        pageModel = Instancio.of(Page.class)
                .ignore(Select.field(Page::getId))
                .supply(Select.field(Page::getName), () -> faker.lorem().word())
                .supply(Select.field(Page::getBody), () -> faker.gameOfThrones().quote())
                .toModel();
    }
}
