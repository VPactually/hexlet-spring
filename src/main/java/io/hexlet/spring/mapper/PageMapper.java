package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.page.PageCreateDTO;
import io.hexlet.spring.dto.page.PageDTO;
import io.hexlet.spring.dto.page.PageUpdateDTO;
import io.hexlet.spring.model.PageModel;
import org.mapstruct.*;

@Mapper(
        uses = {JsonNullableMapper.class, ReferenceMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class PageMapper {
    @Mapping(target = "assignee.id", source = "assigneeId")
    public abstract PageModel map(PageCreateDTO dto);

    @Mapping(source = "assignee.id", target = "assigneeId")
    public abstract PageDTO map(PageModel model);

    @Mapping(source = "assigneeId", target = "assignee")
    public abstract void update(PageUpdateDTO dto, @MappingTarget PageModel model);
}
