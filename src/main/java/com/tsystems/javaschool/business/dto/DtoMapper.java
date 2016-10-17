package com.tsystems.javaschool.business.dto;

/**
 * Created by alex on 28.09.16.
 *
 * Interface for mapping DTOs and Entities
 */
interface DtoMapper<E> {

    /**
     * Converting this DTO object to entity
     * !! Without many-to-many and one-to-many dependencies. Look {@link #addDependencies(Object)}
     * @return Entity object
     */
    E convertToEntity();

    /**
     * Fill DTO object from entity
     * @param entity Entity with data
     */
    void convertToDto(E entity);

    /**
     * Add many-to-many and one-to-many dependencies
     * @param entity entity with dependencies
     * @return this object with dependencies
     */
    DtoMapper addDependencies(E entity);
}
