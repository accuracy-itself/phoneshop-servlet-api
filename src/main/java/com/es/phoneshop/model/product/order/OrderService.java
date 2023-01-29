package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.cart.Cart;

public interface OrderService {
    Order getOrder(Cart cart);

    void placeOrder(Order order);
}
