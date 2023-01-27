package com.es.phoneshop.web;

import com.es.phoneshop.model.product.history.HttpSessionViewHistoryService;
import com.es.phoneshop.model.product.history.ViewHistoryService;
import com.es.phoneshop.model.product.order.ArrayListOrderDao;
import com.es.phoneshop.model.product.order.OrderDao;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrderViewPageServlet extends HttpServlet {
    private OrderDao orderDao;
    private ViewHistoryService viewHistoryService;
    private String ORDER_OVERVIEW_JSP = "/WEB-INF/pages/orderOverview.jsp";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        orderDao = ArrayListOrderDao.getInstance();
        viewHistoryService = HttpSessionViewHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String secureOrderId = request.getPathInfo().substring(1);
        request.setAttribute("order", orderDao.getOrderBySecureId(secureOrderId));
        request.setAttribute("viewHistory", viewHistoryService.getHistory(request));
        request.getRequestDispatcher(ORDER_OVERVIEW_JSP).forward(request, response);
    }
}