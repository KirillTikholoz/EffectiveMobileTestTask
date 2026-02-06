package org.example.mappers;

public interface MappableToDto<S,T> {
    T toDto(S source);
}
