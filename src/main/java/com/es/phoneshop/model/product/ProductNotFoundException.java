package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException {
    private Long productId;

    public ProductNotFoundException(Long productId) {
        this.productId = productId;
    }

    public Long getProductId() {
        return productId;
    }
}
