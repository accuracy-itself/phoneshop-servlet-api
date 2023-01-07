package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.Product;
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
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

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

    private ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        ProductDao productDao = ArrayListProductDao.getInstance();
        Product product = new Product(0L, "test-product", "Samsung Galaxy S", new BigDecimal(100), Currency.getInstance("USD"), 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/0");
        String path = "/WEB-INF/pages/product.jsp";
        when(request.getRequestDispatcher(path)).thenReturn(requestDispatcher);
        servlet.doGet(request, response);
        verify(requestDispatcher).forward(request, response);
    }
}