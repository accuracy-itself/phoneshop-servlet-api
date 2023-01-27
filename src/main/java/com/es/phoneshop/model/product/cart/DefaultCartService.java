package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DefaultCartService implements CartService {
    private static final String CART_SESSION_ATTRIBUTE = DefaultCartService.class.getName() + ".cart";
    private ProductDao productDao;
    private static volatile DefaultCartService instance;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        DefaultCartService localInstance = instance;
        if (localInstance == null) {
            synchronized (DefaultCartService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultCartService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public synchronized Cart getCart(HttpServletRequest request) {
        Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart);
        }
        return cart;
    }

    @Override
    public synchronized void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        List<CartItem> items = cart.getItems();
        Optional<CartItem> cartItem = items.stream()
                .filter(item -> item.getProduct().getDescription().equals(product.getDescription()))
                .findAny();

        if (cartItem.isPresent()) {
            quantity += cartItem.get().getQuantity();
        }

        if (quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock());
        } else {
            if (cartItem.isPresent()) {
                cartItem.get().setQuantity(quantity);
            } else {
                items.add(new CartItem(product, quantity));
            }
        }

        recalculateCart(cart);
    }

    @Override
    public synchronized void update(Cart cart, Long productId, int quantity) throws OutOfStockException {
        Product product = productDao.getProduct(productId);

        List<CartItem> items = cart.getItems();

        if (quantity > product.getStock()) {
            throw new OutOfStockException(product, quantity, product.getStock());
        } else {
            items.stream()
                    .filter(item -> item.getProduct().getCode().equals(product.getCode()))
                    .findAny()
                    .ifPresent(cartItem -> cartItem.setQuantity(quantity));
        }

        recalculateCart(cart);
    }

    @Override
    public synchronized void delete(Cart cart, Long productId) {
        cart.getItems().removeIf(item ->
                item.getProduct().getId().equals(productId));
        recalculateCart(cart);
    }

    private synchronized void recalculateCart(Cart cart) {
        cart.setTotalQuantity(cart.getItems().stream()
                .map(CartItem::getQuantity).mapToInt(Integer::intValue).sum()
        );

        cart.setTotalCost(cart.getItems().stream()
                .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
        );
    }

    @Override
    public void clearCart(Cart cart) {
        cart.setItems(new ArrayList<>());
        cart.setTotalCost(null);
        cart.setTotalQuantity(0);
    }
}
