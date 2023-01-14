package com.es.phoneshop.model.product.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

public class DefaultCartService implements CartService {
    private Cart cart = new Cart();
    private ProductDao productDao;
    private static volatile DefaultCartService instance;

    private DefaultCartService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static DefaultCartService getInstance() {
        DefaultCartService localInstance = instance;
        if (localInstance == null) {
            synchronized (ArrayListProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultCartService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Cart getCart() {
        return null;
    }

    @Override
    public void add(Long productId, int quantity) {
        Product product = productDao.getProduct(productId);

        cart.getItems().add(new CartItem(product, quantity));
    }
}
