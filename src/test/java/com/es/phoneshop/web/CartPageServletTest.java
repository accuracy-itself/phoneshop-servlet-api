package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.history.ViewHistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private CartService cartService;
    @Mock
    private ViewHistoryService viewHistoryService;
    @Mock
    private HttpSession session;

    @InjectMocks
    private CartPageServlet servlet;

    @Before
    public void setUp() throws Exception {
        String path = "/WEB-INF/pages/cart.jsp";
        when(request.getRequestDispatcher(path)).thenReturn(requestDispatcher);
        when(request.getParameterValues("productId")).thenReturn(new String[]{"0"});
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        doNothing().when(cartService).update(any(), anyLong(), anyInt());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("cart"), any());
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPost() throws IOException, ServletException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"1"});

        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
    }

    @Test
    public void testDoPostParseException() throws IOException, ServletException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"kl1kl"});

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testNegativeQuantity() throws IOException, ServletException {
        when(request.getParameterValues("quantity")).thenReturn(new String[]{"-1"});

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
    }
}