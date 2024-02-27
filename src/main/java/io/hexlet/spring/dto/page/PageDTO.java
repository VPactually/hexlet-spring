package io.hexlet.spring.dto.page;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PageDTO {
    private long id;
    private Long assigneeId;
    private String name;
    private String body;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
