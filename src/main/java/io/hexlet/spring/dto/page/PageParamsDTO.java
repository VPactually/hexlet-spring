package io.hexlet.spring.dto.page;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageParamsDTO {
    private String nameCont;
    private Long assigneeId;
    private LocalDate createdAtGt;
    private LocalDate createdAtLt;
}