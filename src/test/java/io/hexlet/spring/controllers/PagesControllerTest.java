package io.hexlet.spring.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.mapper.PageMapper;
import io.hexlet.spring.model.PageModel;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repositories.PageRepository;
import io.hexlet.spring.repositories.UserRepository;
import io.hexlet.spring.util.ModelGenerator;
import io.hexlet.spring.util.UserUtils;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import net.datafaker.Faker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PagesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Faker faker;

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PageMapper mapper;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private ModelGenerator modelGenerator;

    private PageModel testPageModel;

    private User anotherUser;


    private SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor token;

    @BeforeEach
    public void setUp() {
        var user = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(user);
        userRepository.save(anotherUser);
        testPageModel = Instancio.of(modelGenerator.getPageModel()).create();
        testPageModel.setAssignee(user);
        token = jwt().jwt(builder -> builder.subject(userUtils.getTestUser().getEmail()));
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/pages").with(token))
                .andExpect(status().isOk()).andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        pageRepository.save(testPageModel);

        var request = get("/api/pages/{id}", testPageModel.getId()).with(token);
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testPageModel.getName()),
                v -> v.node("body").isEqualTo(testPageModel.getBody()),
                v -> v.node("assigneeId").isEqualTo(testPageModel.getAssignee().getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testPageModel);

        var request = post("/api/pages").with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = pageRepository.findByName(testPageModel.getName()).get();

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(testPageModel.getName());
        assertThat(task.getBody()).isEqualTo(testPageModel.getBody());
        assertThat(task.getAssignee().getId()).isEqualTo(testPageModel.getAssignee().getId());
    }

    @Test
    public void testUpdate() throws Exception {
        pageRepository.save(testPageModel);

        var dto = mapper.map(testPageModel);

        dto.setBody("new body");
        dto.setAssigneeId(anotherUser.getId());

        var request = put("/api/pages/{id}", testPageModel.getId()).with(token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var page = pageRepository.findById(testPageModel.getId()).get();

        assertThat(page.getBody()).isEqualTo(dto.getBody());
        assertThat(page.getAssignee().getId()).isEqualTo(dto.getAssigneeId());
    }

    @Test
    public void testDestroy() throws Exception {
        pageRepository.save(testPageModel);
        var request = delete("/api/pages/{id}", testPageModel.getId()).with(token);
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(pageRepository.existsById(testPageModel.getId())).isFalse();
    }
}
