package com.tsystems.javaschool.business.dto;

/**
 * Created by alex on 28.09.16.
 */
public interface DtoMapper<E> {

    E convertToEntity();

    void convertToDto(E entity);

    DtoMapper addDependencies(E entity);
}
