package com.es.phoneshop.model.product.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ArrayListOrderDao implements OrderDao {
    private List<Order> orders;
    private long maxId;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock writeLock = readWriteLock.writeLock();
    private Lock readLock = readWriteLock.readLock();
    private static volatile OrderDao instance;

    private ArrayListOrderDao() {
        this.orders = new ArrayList<>();
    }

    public static OrderDao getInstance() {
        OrderDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ArrayListOrderDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArrayListOrderDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Order getOrder(Long id) throws OrderNotFoundException {
        Order orderFound;

        readLock.lock();
        try {
            orderFound = orders.stream()
                    .filter(order -> id.equals(order.getId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }

        return orderFound;
    }

    @Override
    public Order getOrderBySecureId(String id) throws OrderNotFoundException {
        Order orderFound;

        readLock.lock();
        try {
            orderFound = orders.stream()
                    .filter(order -> id.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(OrderNotFoundException::new);
        } finally {
            readLock.unlock();
        }

        return orderFound;
    }

    @Override
    public void save(Order order) {
        Long id = order.getId();

        writeLock.lock();

        if (id != null) {
            Optional<Order> orderFound = orders.stream()
                    .filter(ord -> id.equals(ord.getId()))
                    .findAny();

            if (orderFound.isPresent()) {
                orders.set(orders.indexOf(orderFound.get()), order);
            } else {
                orders.add(order);
            }
        } else {
            order.setId(maxId++);
            orders.add(order);
        }

        writeLock.unlock();
    }
}
