package org.example.mappers.impl;

import org.example.dtos.RegistrationUserDto;
import org.example.entities.User;
import org.example.mappers.MappableFromDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper extends
        MappableFromDto<RegistrationUserDto, User>{

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toEntity(RegistrationUserDto dto);
}
