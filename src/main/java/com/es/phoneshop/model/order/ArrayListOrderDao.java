package com.es.phoneshop.model.order;

import com.es.phoneshop.model.ArrayListEntityDao;
import com.es.phoneshop.model.EntityNotFoundException;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

public class ArrayListOrderDao extends ArrayListEntityDao<Order> implements OrderDao {
    private static volatile OrderDao instance;

    private ArrayListOrderDao() {
        this.entities = new ArrayList<>();
        maxId = new AtomicLong(0);
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
    public Order getOrderBySecureId(String id) throws EntityNotFoundException {
        Order orderFound;

        readLock.lock();
        try {
            orderFound = entities.stream()
                    .filter(order -> id.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        } finally {
            readLock.unlock();
        }

        return orderFound;
    }
}
