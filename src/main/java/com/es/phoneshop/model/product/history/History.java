package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class History {
    private List<Product> products;

    public History() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }
}
