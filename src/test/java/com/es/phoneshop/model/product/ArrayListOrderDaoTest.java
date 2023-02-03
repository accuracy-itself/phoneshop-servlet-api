package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.order.ArrayListOrderDao;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderDao;
import com.es.phoneshop.model.product.order.OrderNotFoundException;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArrayListOrderDaoTest {
    private static OrderDao orderDao;

    @BeforeClass
    public static void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        orderDao.save(new Order());
        orderDao.save(new Order());
        Order order = new Order();
        order.setId(1L);
        orderDao.save(order);
    }

    @Test(expected = OrderNotFoundException.class)
    public void findNotExistingOrder() {
        Order order = orderDao.getOrder(0L);
    }

    @Test
    public void findExistingOrder() {
        Order order = orderDao.getOrder(1L);
        assertNotNull(order);
    }

    @Test
    public void testSaveOrder() {
        Order order = new Order();
        long id = 3L;
        order.setId(id);
        orderDao.save(order);
        Order orderFound = orderDao.getOrder(id);
        assertNotNull(orderFound);
        assertEquals(order.getId(), Long.valueOf(id));
    }
}
