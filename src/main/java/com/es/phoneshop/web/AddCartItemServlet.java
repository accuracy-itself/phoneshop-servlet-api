package com.es.phoneshop.web;

import com.es.phoneshop.model.product.cart.Cart;
import com.es.phoneshop.model.product.cart.CartService;
import com.es.phoneshop.model.product.cart.DefaultCartService;
import com.es.phoneshop.model.product.cart.OutOfStockException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class AddCartItemServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = parseProductId(request);
        Cart cart = cartService.getCart(request);

        String quantityString = request.getParameter("quantity");
        int quantity;

        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(quantityString).intValue();
        } catch (ParseException ex) {
            response.sendRedirect(request.getContextPath() + "/products?error=Not a number");
            return;
        }

        try {
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException e) {
            response.sendRedirect(request.getContextPath() + "/products?error=Out of stock, available " + e.getStockAvailable());
            return;
        }

        response.sendRedirect(request.getContextPath() + "/products?message=Product added to cart.");
    }

    private Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }
}
