package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class HttpSessionHistoryService implements HistoryService{
    private static final String HISTORY_SESSION_ATTRIBUTE = HttpSessionHistoryService.class.getName() + ".history";
    private ProductDao productDao;
    private static volatile HttpSessionHistoryService instance;

    private HttpSessionHistoryService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static HttpSessionHistoryService getInstance() {
        HttpSessionHistoryService localInstance = instance;
        if (localInstance == null) {
            synchronized (ArrayListProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new HttpSessionHistoryService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public synchronized History getHistory(HttpServletRequest request) {
        History history = (History) request.getSession().getAttribute(HISTORY_SESSION_ATTRIBUTE);
        if(history == null) {
            request.getSession().setAttribute(HISTORY_SESSION_ATTRIBUTE, history = new History());
        }
        return history;
    }

    @Override
    public synchronized void add(History history, Long productId) {
        Product product = productDao.getProduct(productId);

        List<Product> products = history.getProducts();
        int index = products.indexOf(product);
        if(index >= 0) {
            products.remove(product);
        }

        products.add(0, product);

        if(products.size() > 3)
            products.remove(3);
    }
}
