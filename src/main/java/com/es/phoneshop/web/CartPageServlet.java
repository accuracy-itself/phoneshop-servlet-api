package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;
import com.es.phoneshop.model.product.history.HttpSessionViewHistoryService;
import com.es.phoneshop.model.product.history.ViewHistoryService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

public class CartPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;
    private ViewHistoryService viewHistoryService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        viewHistoryService = HttpSessionViewHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        request.setAttribute("cart", cartService.getCart(request));
        request.setAttribute("viewHistory", viewHistoryService.getHistory(request));
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productIds = request.getParameterValues("productId");
        String[]  quantities = request.getParameterValues("quantity");

        Map<Long, String> errors = new HashMap<>();
        for(int i = 0; i < productIds.length; i++) {
            Long productId = Long.valueOf(productIds[i]);

            int quantity;

            try {
                NumberFormat format = NumberFormat.getInstance(request.getLocale());
                quantity = format.parse(quantities[i]).intValue();
                cartService.update(cartService.getCart(request), productId, quantity);
            } catch (ParseException ex) {
                errors.put(productId, "Not a number");
            } catch (OutOfStockException e) {
                errors.put(productId, "Out of stock, available " + e.getStockAvailable());
            }
        }

        if(errors.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart?message=Cart updated successfully.");
        } else {
            request.setAttribute("errors", errors);
            doGet(request, response);
        }
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }
}
