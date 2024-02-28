package io.hexlet.spring.component;

import io.hexlet.spring.dto.user.UserCreateDTO;
import io.hexlet.spring.mapper.UserMapper;
import io.hexlet.spring.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DataInitializer implements ApplicationRunner {


    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserMapper userMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        var userData = new UserCreateDTO();
        userData.setName("hexlet");
        userData.setEmail("hexlet@example.com");
        userData.setPasswordDigest("123");
        var user = userMapper.map(userData);
        userRepository.save(user);
    }
}
