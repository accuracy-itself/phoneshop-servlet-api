package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import java.io.Serializable;
import java.util.Objects;

public class ViewHistoryItem implements Serializable {
    public Product product;

    public ViewHistoryItem(Product product) {
        this.product = product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewHistoryItem that = (ViewHistoryItem) o;
        return product.getCode().equals(that.product.getCode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    public Product getProduct() {
        return product;
    }
}
