package com.es.phoneshop.model.history;

import javax.servlet.http.HttpServletRequest;

public interface ViewHistoryService {
    ViewHistory getHistory(HttpServletRequest request);
    void add(ViewHistory viewHistory, Long productId);
}
