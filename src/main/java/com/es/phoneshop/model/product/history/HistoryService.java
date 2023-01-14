package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.OutOfStockException;

import javax.servlet.http.HttpServletRequest;

public interface HistoryService {
    History getHistory(HttpServletRequest request);
    void add(History history, Long productId);
}
