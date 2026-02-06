package org.example.mappers.impl;

import org.example.dtos.TaskDto;
import org.example.entitis.Task;
import org.example.enums.Priority;
import org.example.enums.Status;
import org.example.mappers.MappableFromDto;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TaskMapper extends
        MappableFromDto<TaskDto, Task>{

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "status", source = "status", qualifiedByName = "toStatus")
    @Mapping(target = "priority", source = "priority", qualifiedByName = "toPriority")
    Task toEntity(TaskDto dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @InheritConfiguration(name = "toEntity")
    void updateFromDto(TaskDto dto, @MappingTarget Task task);

    @Named("toStatus")
    default Status toStatus(String status) {
        return status == null ? null : Status.valueOf(status.toUpperCase());
    }

    @Named("toPriority")
    default Priority toPriority(String priority) {
        return priority == null ? null : Priority.valueOf(priority.toUpperCase());
    }
}
