package io.hexlet.spring.dto.page;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

@Getter
@Setter
public class PageUpdateDTO {
    @NotNull
    private Long assigneeId;
    @NotNull
    private JsonNullable<String> body;
}
