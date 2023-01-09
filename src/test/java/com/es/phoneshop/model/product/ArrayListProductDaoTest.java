package com.es.phoneshop.model.product;

import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class ArrayListProductDaoTest
{
    private static ProductDao productDao;

    @BeforeClass
    public static void setup() {
        productDao = ArrayListProductDao.getInstance();
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(12L,"test-product", "HTC Super Mega", new BigDecimal(400), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(13L,"test-product", "Iphone", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(14L,"test-product", "Super Mega Device", new BigDecimal(500), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
    }

    @Test
    public void testFindCorrectProducts() {
        List<Product> products = productDao.findProducts(null, null, null);
        assertFalse(products.isEmpty());

        Optional<Product> productFoundOpt = products.stream()
                .filter(product -> product.getPrice() == null || product.getStock() <=0)
                .findAny();

        assertFalse(productFoundOpt.isPresent());
    }

    @Test
    public void testSaveNewProduct() throws ProductNotFoundException {
        Long id = 100L;
        Currency usd = Currency.getInstance("USD");
        Product product = new Product(id,"test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg");
        productDao.save(product);

        assertTrue(product.getId() >= 0);
        Product result = productDao.getProduct(product.getId());
        assertNotNull(result);
        assertEquals("test-product", product.getCode());
        productDao.delete(id);
    }

    @Test
    public void testSaveProductWithExistingId() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        productDao.save(new Product(0L,"test-product", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.save(new Product(0L,"test-product-with-existing-id", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        Product productFound = productDao.getProduct(0L);
        assertEquals("test-product-with-existing-id", productFound.getCode());
        productDao.delete(0L);
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

    @Test(expected = ProductNotFoundException.class)
    public void testDeleteProduct() throws ProductNotFoundException {
        Currency usd = Currency.getInstance("USD");
        long id = 0L;
        productDao.save(new Product(id,"test-product-with-existing-id", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        productDao.delete(id);
        productDao.getProduct(id);
    }

    @Test
    public void testSearchForProducts(){
        assertEquals(2, productDao.findProducts("S", null, null).size());
    }

    @Test
    public void testSortProducts(){
        List<Product> products = productDao.findProducts(null, SortField.PRICE, SortOrder.DESC);
        List<BigDecimal> prices = new ArrayList<>(), pricesFound = new ArrayList<>();
        prices.add(new BigDecimal(500));
        prices.add(new BigDecimal(400));
        prices.add(new BigDecimal(100));

        for (Product product: products) {
            pricesFound.add(product.getPrice());
        }

        assertEquals(prices, pricesFound);
    }

}
