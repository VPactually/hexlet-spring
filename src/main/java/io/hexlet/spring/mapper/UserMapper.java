package io.hexlet.spring.mapper;

import io.hexlet.spring.dto.user.UserCreateDTO;
import io.hexlet.spring.dto.user.UserDTO;
import io.hexlet.spring.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class UserMapper {
    
    public abstract User map(UserCreateDTO dto);
    public abstract UserDTO map(User model);
}
