package io.hexlet.spring.util;


import io.hexlet.spring.model.PageModel;
import io.hexlet.spring.model.User;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import net.datafaker.Faker;
import org.instancio.Instancio;
import org.instancio.Model;
import org.instancio.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Getter
@Component
public class ModelGenerator {
    private Model<PageModel> pageModel;
    private Model<User> userModel;

    @Autowired
    private Faker faker;

    @PostConstruct
    private void init() {
        userModel = Instancio.of(User.class)
                .ignore(Select.field(User::getId))
                .ignore(Select.field(User::getPageModels))
                .supply(Select.field(User::getName), () -> faker.name().fullName())
                .supply(Select.field(User::getEmail), () -> faker.internet().emailAddress())
                .supply(Select.field(User::getPasswordDigest), () -> faker.internet().password(3, 100))
                .toModel();


        pageModel = Instancio.of(PageModel.class)
                .ignore(Select.field(PageModel::getId))
                .supply(Select.field(PageModel::getName), () -> faker.lorem().word())
                .supply(Select.field(PageModel::getBody), () -> faker.gameOfThrones().quote())
                .toModel();
    }
}
