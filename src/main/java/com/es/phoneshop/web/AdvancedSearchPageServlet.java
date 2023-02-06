package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SortField;
import com.es.phoneshop.model.product.SortOrder;
import com.es.phoneshop.model.history.HttpSessionViewHistoryService;
import com.es.phoneshop.model.history.ViewHistoryService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;
    private ViewHistoryService viewHistoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        viewHistoryService = HttpSessionViewHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String query = request.getParameter("query");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("products", productDao.findProducts(query,
                (sortField != null) ? SortField.valueOf(sortField.toUpperCase()) : null,
                (sortOrder != null) ? SortOrder.valueOf(sortOrder.toUpperCase()) : null));

        request.setAttribute("viewHistory", viewHistoryService.getHistory(request));

        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> errors = new HashMap<>();
        BigDecimal minPrice = getBigDecimalParameter(request, "minPrice", errors);
        BigDecimal maxPrice = getBigDecimalParameter(request, "maxPrice", errors);
        Boolean wordMatch = Boolean.valueOf(request.getParameter("wordMatching"));
        String query = request.getParameter("description");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("viewHistory", viewHistoryService.getHistory(request));

        if (errors.isEmpty()) {
            request.setAttribute("products", productDao.findProductsAdvanced(query,
                    wordMatch,
                    (sortField != null) ? SortField.valueOf(sortField.toUpperCase()) : null,
                    (sortOrder != null) ? SortOrder.valueOf(sortOrder.toUpperCase()) : null,
                    minPrice,
                    maxPrice));

            request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
        } else {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
        }
    }

    private BigDecimal getBigDecimalParameter(HttpServletRequest request, String name, Map<String, String> errors) {
        String value = request.getParameter(name);
        if (value != null && !value.isEmpty()) {
            try {
                return new BigDecimal(value);
            } catch (Exception e) {
                errors.put(name, "Wrong value");
            }
        }

        return null;
    }
}
