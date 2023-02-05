package com.es.phoneshop.model.order;

import com.es.phoneshop.model.EntityDao;
import com.es.phoneshop.model.EntityNotFoundException;

public interface OrderDao extends EntityDao<Order> {
    Order getOrderBySecureId(String id) throws EntityNotFoundException;
}
