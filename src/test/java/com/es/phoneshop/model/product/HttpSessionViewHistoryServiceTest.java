package com.es.phoneshop.model.product;

import com.es.phoneshop.model.product.history.HttpSessionViewHistoryService;
import com.es.phoneshop.model.product.history.ViewHistory;
import com.es.phoneshop.model.product.history.ViewHistoryService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.text.View;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HttpSessionViewHistoryServiceTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpSession session;

    private ViewHistoryService viewHistoryService;
    private ProductDao productDao;

    @Before
    public void setup() {
        viewHistoryService = HttpSessionViewHistoryService.getInstance();
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(12L, "test-product", "HTC Super Mega", new BigDecimal(400), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(13L, "test-product2", "Iphone", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(14L, "test-product3", "Super Mega Device", new BigDecimal(500), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(15L, "test-product4", "Max Super Mega Device", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));

        when(request.getSession()).thenReturn(session);
    }

    @Test
    public void testGetHistory() {
        ViewHistory cart = viewHistoryService.getHistory(request);

        assertNotNull(cart);
    }

    @Test
    public void testAddTooMuchProducts(){
        ViewHistory viewHistory = viewHistoryService.getHistory(request);

        viewHistoryService.add(viewHistory, 12L);
        viewHistoryService.add(viewHistory, 13L);
        viewHistoryService.add(viewHistory, 14L);
        viewHistoryService.add(viewHistory, 15L);

        assertEquals(3, viewHistory.getItems().size());
    }

    @Test
    public void testAddSameProduct(){
        ViewHistory viewHistory = viewHistoryService.getHistory(request);

        viewHistoryService.add(viewHistory, 12L);
        viewHistoryService.add(viewHistory, 13L);
        viewHistoryService.add(viewHistory, 12L);

        assertEquals(Long.valueOf(12L), viewHistory.getItems().get(0).getProduct().getId());
    }
}
