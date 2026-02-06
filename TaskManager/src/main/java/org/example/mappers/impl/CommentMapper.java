package org.example.mappers.impl;

import org.example.dtos.request.CommentDto;
import org.example.dtos.response.CommentResponseDto;
import org.example.entitis.Comment;
import org.example.mappers.MappableFromDto;
import org.example.mappers.MappableToDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends
        MappableFromDto<CommentDto, Comment>, MappableToDto<Comment, CommentResponseDto> {

    @Mapping(target = "author", ignore = true)
    Comment toEntity(CommentDto dto);
}
