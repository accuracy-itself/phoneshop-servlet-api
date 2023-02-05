package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.history.ViewHistory;
import com.es.phoneshop.model.history.ViewHistoryService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
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
import java.util.List;

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

    @Mock
    OrderService orderService;
    @Mock
    Order order;

    @Mock
    ViewHistoryService viewHistoryService;

    @Mock
    ViewHistory history;

    @Mock
    Cart cart;

    @Mock
    List<CartItem> cartItems;
    @InjectMocks
    @Spy
    private CheckoutPageServlet servlet;

    private String CHECKOUT_JSP = "/WEB-INF/pages/checkout.jsp";

    @Before
    public void setUp() throws Exception {
        String path = "/WEB-INF/pages/checkout.jsp";
        when(cartService.getCart(request)).thenReturn(cart);
        when(orderService.getOrder(cart)).thenReturn(order);
        when(request.getRequestDispatcher(CHECKOUT_JSP)).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cart"), any());
        verify(response).sendRedirect(any());
    }

    @Test
    public void testDoGetWithAttributes() throws ServletException, IOException {
        when(cart.getItems()).thenReturn(cartItems);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("viewHistory"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithoutRequestParameters() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithRequestParameters() throws ServletException, IOException {
        when(request.getParameter("firstName")).thenReturn("name");
        when(request.getParameter("lastName")).thenReturn("surname");
        when(request.getParameter("phone")).thenReturn("777");
        when(request.getParameter("deliveryAddress")).thenReturn("best address");
        when(request.getParameter("paymentMethod")).thenReturn("CASH");
        when(request.getParameter("deliveryDate")).thenReturn("2022-11-11");

        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
    }
}