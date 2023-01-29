package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.history.ViewHistory;
import com.es.phoneshop.model.product.history.ViewHistoryService;
import com.es.phoneshop.model.product.order.Order;
import com.es.phoneshop.model.product.order.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;

    @Mock
    CartService cartService;

    OrderService orderService;
    Order order;

    @Mock
    ViewHistoryService viewHistoryService;

    @Mock
    ViewHistory history;

    @Mock
    Cart cart;

    @InjectMocks
    @Spy
    private CheckoutPageServlet servlet;

    @Before
    public void setUp() throws Exception {
        String path = "/WEB-INF/pages/checkout.jsp";
        when(cartService.getCart(request)).thenReturn(cart);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("cart"), any());
        verify(response).sendRedirect(any());
    }
}