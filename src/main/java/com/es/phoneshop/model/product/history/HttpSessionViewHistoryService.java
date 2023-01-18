package com.es.phoneshop.model.product.history;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class HttpSessionViewHistoryService implements ViewHistoryService {
    private static final String HISTORY_SESSION_ATTRIBUTE = HttpSessionViewHistoryService.class.getName() + ".history";
    private ProductDao productDao;
    private static volatile HttpSessionViewHistoryService instance;

    private HttpSessionViewHistoryService() {
        productDao = ArrayListProductDao.getInstance();
    }

    public static HttpSessionViewHistoryService getInstance() {
        HttpSessionViewHistoryService localInstance = instance;
        if (localInstance == null) {
            synchronized (ArrayListProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new HttpSessionViewHistoryService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public synchronized ViewHistory getHistory(HttpServletRequest request) {
        ViewHistory viewHistory = (ViewHistory) request.getSession().getAttribute(HISTORY_SESSION_ATTRIBUTE);
        if (viewHistory == null) {
            viewHistory = new ViewHistory();
            request.getSession().setAttribute(HISTORY_SESSION_ATTRIBUTE, viewHistory);
        }
        return viewHistory;
    }

    @Override
    public synchronized void add(ViewHistory history, Long productId) {
        Product product = productDao.getProduct(productId);

        List<ViewHistoryItem> viewHistory = history.getItems();
        ViewHistoryItem viewHistoryItem = new ViewHistoryItem(product);
        int index = viewHistory.indexOf(viewHistoryItem);
        if (index >= 0) {
            viewHistory.remove(index);
        }

        viewHistory.add(0, viewHistoryItem);

        if (viewHistory.size() > 3) {
            viewHistory.remove(3);
        }
    }
}
