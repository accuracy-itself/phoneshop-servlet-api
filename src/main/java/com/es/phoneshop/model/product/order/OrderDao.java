package com.es.phoneshop.model.product.order;

public interface OrderDao {
    Order getOrder(Long id) throws OrderNotFoundException;

    void save(Order order);
}
