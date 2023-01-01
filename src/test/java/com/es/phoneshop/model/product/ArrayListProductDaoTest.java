package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }

    @Test
    public void testFindProductsNotEmpty() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        Product product = new Product("test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertTrue(product.getId() >= 0);
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product", product.getCode());
    }

    @Test
    public void testSaveProductWithExistingId() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        long id = 0L;
        Product product = new Product(id,"test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        Product productWithExistingId = new Product(id,"test-product-with-existing-id", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);
        productDao.save(productWithExistingId);

        Product productFound = productDao.getProduct(id);
        assertEquals("test-product-with-existing-id", productFound.getCode());
    }

    @Test(expected = ProductNotFoundException.class)
    public void testFindProductWithZeroStock() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        long id = 12L;
        Product product = new Product(id,"test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        Product productFound = productDao.getProduct(id);
    }

    @Test
    public void testGetSetProductFields() {
        Long id = 0L;
        String code = "test-product";
        String description = "My Super Device";
        BigDecimal price = new BigDecimal(200089);
        Currency currency = Currency.getInstance("USD");
        int stock = 2;
        String imageUrl = "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg";

        Product product= new Product();
        product.setId(id);
        product.setCode(code);
        product.setDescription(description);
        product.setPrice(price);
        product.setCurrency(currency);
        product.setStock(stock);
        product.setImageUrl(imageUrl);
        
        assertEquals(id, product.getId());
        assertEquals(code, product.getCode());
        assertEquals(description, product.getDescription());
        assertEquals(price, product.getPrice());
        assertEquals(currency, product.getCurrency());
        assertEquals(stock, product.getStock());
        assertEquals(imageUrl, product.getImageUrl());
    }
}
