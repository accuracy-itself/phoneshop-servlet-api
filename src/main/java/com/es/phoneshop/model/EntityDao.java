package com.es.phoneshop.model;

public interface EntityDao<T> {
    T getEntity(Long id) throws EntityNotFoundException;

    void save(T entity);
}