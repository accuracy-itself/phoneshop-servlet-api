package com.es.phoneshop.model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListEntityDao<T extends Entity> implements EntityDao<T> {
    protected List<T> entities;
    protected AtomicLong maxId;
    protected ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    protected Lock writeLock = readWriteLock.writeLock();
    protected Lock readLock = readWriteLock.readLock();

    @Override
    public T getEntity(Long id) throws EntityNotFoundException {
        T entityFound;

        readLock.lock();
        try {
            entityFound = entities.stream()
                    .filter(entity -> id.equals(entity.getId()))
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        } finally {
            readLock.unlock();
        }

        return entityFound;
    }

    @Override
    public void save(T entity) {
        Long id = entity.getId();

        writeLock.lock();

        if (id != null) {
            Optional<T> entityFound = entities.stream()
                    .filter(prod -> id.equals(prod.getId()))
                    .findAny();

            if (entityFound.isPresent()) {
                entities.set(entities.indexOf(entityFound.get()), entity);
            } else {
                entities.add(entity);
            }
        } else {
            entity.setId(maxId.incrementAndGet());
            entities.add(entity);
        }

        writeLock.unlock();
    }
}
