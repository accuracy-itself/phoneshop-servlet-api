package com.es.phoneshop.web;

import com.es.phoneshop.model.history.ViewHistory;
import com.es.phoneshop.model.history.ViewHistoryService;
import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderViewPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    private OrderDao orderDao;

    @Mock
    ViewHistoryService viewHistoryService;

    @Mock
    ViewHistory history;

    @Mock
    HttpSession session;

    private String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";

    @InjectMocks
    @Spy
    private OrderViewPageServlet servlet;

    @Before
    public void setUp() throws Exception {
        servlet.init(config);
        when(request.getPathInfo()).thenReturn("/qwerty");
        orderDao = ArrayListOrderDao.getInstance();
        Order order = new Order();
        orderDao.save(order);
        order.setSecureId("qwerty");
        when(request.getRequestDispatcher(ORDER_OVERVIEW_JSP)).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);
        verify(request).setAttribute(eq("order"), any());
        verify(requestDispatcher).forward(request, response);
    }
}