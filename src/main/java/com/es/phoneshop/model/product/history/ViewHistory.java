package com.es.phoneshop.model.product.history;

import java.util.ArrayList;
import java.util.List;

public class ViewHistory {
    private List<ViewHistoryItem> items;

    public ViewHistory() {
        this.items = new ArrayList<>();
    }

    public List<ViewHistoryItem> getItems() {
        return items;
    }
}
