package io.hexlet.spring.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.hexlet.spring.mapper.PageMapper;
import io.hexlet.spring.model.Page;
import io.hexlet.spring.model.User;
import io.hexlet.spring.repositories.PageRepository;
import io.hexlet.spring.repositories.UserRepository;
import io.hexlet.spring.util.ModelGenerator;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;

import net.datafaker.Faker;

import static org.assertj.core.api.Assertions.assertThat;
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
    private ModelGenerator modelGenerator;

    private Page testPage;

    private User anotherUser;

    @BeforeEach
    public void setUp() {
        var user = Instancio.of(modelGenerator.getUserModel()).create();
        anotherUser = Instancio.of(modelGenerator.getUserModel()).create();
        userRepository.save(user);
        userRepository.save(anotherUser);
        testPage = Instancio.of(modelGenerator.getPageModel()).create();
        testPage.setAssignee(user);
    }

    @Test
    public void testIndex() throws Exception {
        var result = mockMvc.perform(get("/api/pages"))
                .andExpect(status().isOk()).andReturn();

        var body = result.getResponse().getContentAsString();
        assertThatJson(body).isArray();
    }

    @Test
    public void testShow() throws Exception {

        pageRepository.save(testPage);

        var request = get("/api/pages/{id}", testPage.getId());
        var result = mockMvc.perform(request)
                .andExpect(status().isOk())
                .andReturn();
        var body = result.getResponse().getContentAsString();

        assertThatJson(body).and(
                v -> v.node("name").isEqualTo(testPage.getName()),
                v -> v.node("body").isEqualTo(testPage.getBody()),
                v -> v.node("assigneeId").isEqualTo(testPage.getAssignee().getId())
        );
    }

    @Test
    public void testCreate() throws Exception {
        var dto = mapper.map(testPage);

        var request = post("/api/pages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isCreated());

        var task = pageRepository.findByName(testPage.getName()).get();

        assertThat(task).isNotNull();
        assertThat(task.getName()).isEqualTo(testPage.getName());
        assertThat(task.getBody()).isEqualTo(testPage.getBody());
        assertThat(task.getAssignee().getId()).isEqualTo(testPage.getAssignee().getId());
    }

    @Test
    public void testUpdate() throws Exception {
        pageRepository.save(testPage);

        var dto = mapper.map(testPage);

        dto.setBody("new body");
        dto.setAssigneeId(anotherUser.getId());

        var request = put("/api/pages/{id}", testPage.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(dto));

        mockMvc.perform(request)
                .andExpect(status().isOk());

        var page = pageRepository.findById(testPage.getId()).get();

        assertThat(page.getBody()).isEqualTo(dto.getBody());
        assertThat(page.getAssignee().getId()).isEqualTo(dto.getAssigneeId());
    }

    @Test
    public void testDestroy() throws Exception {
        pageRepository.save(testPage);
        var request = delete("/api/pages/{id}", testPage.getId());
        mockMvc.perform(request)
                .andExpect(status().isNoContent());

        assertThat(pageRepository.existsById(testPage.getId())).isFalse();
    }
}
