package com.es.phoneshop.model.product.order;

public class OrderNotFoundException extends RuntimeException {
    private Long orderId;

    public OrderNotFoundException(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
