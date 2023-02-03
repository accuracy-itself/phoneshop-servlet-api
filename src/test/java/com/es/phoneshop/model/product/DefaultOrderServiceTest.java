package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.order.ArrayListOrderDao;
import com.es.phoneshop.model.product.order.DefaultOrderService;
import com.es.phoneshop.model.product.order.Order;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private DefaultOrderService orderService;
    private ProductDao productDao;

    private CartService cartService;
    private Cart cart;

    @Before
    public void setup() throws OutOfStockException {
        when(request.getSession()).thenReturn(session);
        orderService = DefaultOrderService.getInstance();
        cartService = DefaultCartService.getInstance();
        cart = DefaultCartService.getInstance().getCart(request);
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(12L, "test-product", "HTC Super Mega", new BigDecimal(400), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(13L, "test-product2", "Iphone", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(14L, "test-product3", "Super Mega Device", new BigDecimal(500), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        cartService.add(cart,12L, 2);
        cartService.add(cart,13L, 1);
        cartService.add(cart,14L, 3);
    }

    @Test
    public void testGetOrder() {
        Order order = orderService.getOrder(cart);
        assertNotNull(order);
        assertEquals(order.getSubTotal(), new BigDecimal(2400));
    }

    @Test
    public void testPlaceOrder() {
        Order order = orderService.getOrder(cart);
        orderService.placeOrder(order);
        Order orderFound = ArrayListOrderDao.getInstance().getOrder(order.getId());
        assertNotNull(orderFound);
    }
}
