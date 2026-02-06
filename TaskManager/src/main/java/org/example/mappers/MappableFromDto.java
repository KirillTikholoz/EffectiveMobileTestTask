package org.example.mappers;

public interface MappableFromDto<S,T> {
    T toEntity(S source);
}
