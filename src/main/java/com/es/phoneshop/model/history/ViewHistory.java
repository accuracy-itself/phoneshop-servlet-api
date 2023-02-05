package com.es.phoneshop.model.history;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewHistory implements Serializable {
    private List<ViewHistoryItem> items;

    public ViewHistory() {
        this.items = new ArrayList<>();
    }

    public List<ViewHistoryItem> getItems() {
        return items;
    }
}
