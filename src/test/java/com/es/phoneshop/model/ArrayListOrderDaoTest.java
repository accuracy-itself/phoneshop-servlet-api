package com.es.phoneshop.model;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ArrayListOrderDaoTest {
    private static OrderDao orderDao;
    private static final Long existingId = 1L;
    private static final Long notExistingId = 0L;
    private static final String existingSecureId = "super-secure";
    private static final String notExistingSecureId = "less secure";

    @BeforeClass
    public static void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        orderDao.save(new Order());
        orderDao.save(new Order());
        Order order = new Order();
        order.setId(existingId);
        Order orderWithSecureId = new Order();
        orderWithSecureId.setSecureId(existingSecureId);
        orderDao.save(order);
        orderDao.save(orderWithSecureId);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testfindNotExistingOrder() {
        Order order = orderDao.getEntity(notExistingId);
    }

    @Test
    public void testfindExistingOrder() {
        Order order = orderDao.getEntity(existingId);
        assertNotNull(order);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testfindNotExistingOrderBySecureId() {
        Order order = orderDao.getOrderBySecureId(notExistingSecureId);
    }

    @Test
    public void testfindExistingOrderBySecureId() {
        Order order = orderDao.getOrderBySecureId(existingSecureId);
        assertNotNull(order);
    }

    @Test
    public void testSaveOrder() {
        Order order = new Order();
        long id = 3L;
        order.setId(id);
        orderDao.save(order);
        Order orderFound = orderDao.getEntity(id);
        assertNotNull(orderFound);
        assertEquals(order.getId(), Long.valueOf(id));
    }
}
