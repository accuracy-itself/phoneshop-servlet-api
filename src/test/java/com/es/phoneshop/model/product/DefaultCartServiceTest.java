package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
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
public class DefaultCartServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private DefaultCartService cartService;
    private ProductDao productDao;

    @Before
    public void setup() {
        cartService = DefaultCartService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(12L, "test-product", "HTC Super Mega", new BigDecimal(400), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(13L, "test-product2", "Iphone", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(14L, "test-product3", "Super Mega Device", new BigDecimal(500), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetCart() {
        Cart cart = cartService.getCart(request);

        assertNotNull(cart);
    }

    @Test
    public void testAddProducts() throws OutOfStockException {
        Cart cart = cartService.getCart(request);

        cartService.add(cart, 12L, 10);
        cartService.add(cart, 13L, 10);
        cartService.add(cart, 14L, 15);

        assertEquals(3, cart.getItems().size());
    }

    @Test
    public void testAddSameProduct() throws OutOfStockException {
        Cart cart = cartService.getCart(request);

        cartService.add(cart, 12L, 10);
        cartService.add(cart, 12L, 10);

        assertEquals(20, cart.getItems().get(0).getQuantity());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddProductOutOfStock() throws OutOfStockException {
        Cart cart = cartService.getCart(request);

        cartService.add(cart, 12L, 1000);
    }

    @Test
    public void testUpdate() throws OutOfStockException, ProductNotFoundException {
        Cart cart = cartService.getCart(request);
        cartService.add(cart, 12L, 10);
        cartService.update(cart, 12L, 15);
        assertEquals(15, cart.getTotalQuantity());
    }
}
