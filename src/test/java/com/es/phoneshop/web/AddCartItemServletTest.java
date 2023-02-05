package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AddCartItemServletTest {
    @Mock
    private ServletConfig config;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    @Mock
    private CartService cartService;

    private Long productId = 0L;

    @InjectMocks
    private AddCartItemServlet servlet;

    @Before
    public void setUp() throws Exception {
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test
    public void testDoPost() throws IOException {
        when(request.getParameter("quantity")).thenReturn("2");
        servlet.doPost(request, response);
        verify(response).sendRedirect(request.getContextPath() + "/products?message=Product added to cart.");
    }

    @Test
    public void testDoPostParseException() throws IOException {
        when(request.getParameter("quantity")).thenReturn("ggg2sfd");
        servlet.doPost(request, response);
        verify(response).sendRedirect(request.getContextPath() + "/products?error=Not a number");
    }
}