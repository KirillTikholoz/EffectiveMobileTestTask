package org.example.mappers.impl;

import org.example.dtos.CommentDto;
import org.example.entitis.Comment;
import org.example.mappers.MappableFromDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends
        MappableFromDto<CommentDto, Comment> {

    @Mapping(target = "author", ignore = true)
    Comment toEntity(CommentDto dto);
}
