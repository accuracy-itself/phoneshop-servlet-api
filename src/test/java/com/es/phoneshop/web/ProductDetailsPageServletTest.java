package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private HttpSession session;

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        ProductDao productDao = ArrayListProductDao.getInstance();
        Product product = new Product(0L, "test-product", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        String path = "/WEB-INF/pages/product.jsp";
        when(request.getPathInfo()).thenReturn("/0");
        when(request.getRequestDispatcher(path)).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostWithoutErrors() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("2");

        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
    }

    @Test
    public void testDoPostParseException() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("fds2");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), any());
    }

    @Test
    public void testDoPostNegativeQuantity() throws ServletException, IOException {
        when(request.getParameter("quantity")).thenReturn("-2");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("error"), any());
    }
}