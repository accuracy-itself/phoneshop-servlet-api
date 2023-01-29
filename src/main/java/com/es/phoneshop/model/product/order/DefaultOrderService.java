package com.es.phoneshop.model.product.order;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartItem;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private OrderDao orderDao;

    private static volatile DefaultOrderService instance;

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    public static DefaultOrderService getInstance() {
        DefaultOrderService localInstance = instance;
        if (localInstance == null) {
            synchronized (ArrayListProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultOrderService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Order getOrder(Cart cart) {
        Order order = new Order();
        order.setItems(cart.getItems().stream().map(item -> {
            try {
                return item.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList()));
        order.setSubTotal(cart.getTotalCost());
        order.setDeliveryCost(calculateDeliveryCost());
        order.setTotalQuantity(cart.getTotalQuantity());
        order.setTotalCost(calculateTotalCost(order));
        return order;
    }

    private BigDecimal calculateDeliveryCost() {
        return new BigDecimal(1);
    }

    private BigDecimal calculateTotalCost(Order order) {
        return order.getDeliveryCost().add(order.getSubTotal());
    }

    @Override
    public synchronized void placeOrder(Order order) {
        order.setSecureId(UUID.randomUUID().toString());
        orderDao.save(order);
    }
}
