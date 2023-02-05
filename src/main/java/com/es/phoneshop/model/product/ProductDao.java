package com.es.phoneshop.model.product;

import com.es.phoneshop.model.EntityDao;

import java.util.List;

public interface ProductDao extends EntityDao<Product> {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void delete(Long id);
}
