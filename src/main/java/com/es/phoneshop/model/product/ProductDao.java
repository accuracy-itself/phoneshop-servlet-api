package com.es.phoneshop.model.product;

import com.es.phoneshop.model.EntityDao;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends EntityDao<Product> {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> findProductsAdvanced(String query, Boolean wordAllMatch, SortField sortField, SortOrder sortOrder, BigDecimal minPrice, BigDecimal maxPrice);

    void delete(Long id);
}
