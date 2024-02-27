package io.hexlet.spring.dto.page;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageCreateDTO {
    @NotNull
    private Long assigneeId;
    @NotNull
    private String name;
    @NotBlank
    private String body;
}
