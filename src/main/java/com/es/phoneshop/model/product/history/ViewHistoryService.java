package com.es.phoneshop.model.product.history;

import javax.servlet.http.HttpServletRequest;

public interface ViewHistoryService {
    ViewHistory getHistory(HttpServletRequest request);
    void add(ViewHistory viewHistory, Long productId);
}
