package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.Comparator;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {
    private List<Product> products;
    private long maxId;
    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private Lock writeLock = readWriteLock.writeLock();
    private Lock readLock = readWriteLock.readLock();

    public ArrayListProductDao() {
        this.products = new ArrayList<>();
        saveSampleProducts();
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        Product productFound;

        readLock.lock();
        productFound = products.stream()
                .filter(product -> id.equals(product.getId()))
                .filter(product -> product.getStock() > 0)
                .filter(product -> product.getPrice() != null)
                .findAny()
                .orElseThrow(() -> new ProductNotFoundException());
        readLock.unlock();

        return productFound;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        try {
            Comparator<Product> comparator = Comparator.comparing(product -> {
                    if(SortField.description == sortField)
                        return (Comparable) product.getDescription();

                    if(SortField.price == sortField)
                        return (Comparable) product.getPrice();

                    else if(query != null && !query.equals(""))
                        return (Comparable)Arrays.stream(query.split(" "))
                                .filter(word -> product.getDescription().contains(word)).count();

                    else
                        return(Comparable) product.getId();
            });

            if(query == null || query.equals(""))
                return products.stream()
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> product.getPrice() != null)
                        .sorted((sortOrder == SortOrder.desc) ? comparator.reversed() : comparator)
                        .collect(Collectors.toList());
            else
                return products.stream()
                        .filter(product -> product.getStock() > 0)
                        .filter(product -> product.getPrice() != null)
                        .filter(product -> Arrays.stream(query.split(" "))
                                .anyMatch(word -> product.getDescription().contains(word)))
                        .sorted((sortOrder == SortOrder.desc) ? comparator.reversed() : comparator)
                        .collect(Collectors.toList());
        }
        finally{
            readLock.unlock();
        }
    }

    @Override
    public void save(Product product) {
        Long id = product.getId();

        writeLock.lock();

        if(id != null) {
            Optional<Product> productFoundOpt = products.stream()
                    .filter(productFound -> id.equals(productFound.getId()))
                    .findAny();

            if(productFoundOpt.isPresent())
                products.set(products.indexOf(productFoundOpt.get()), product);
            else
                products.add(product);
        }
        else {
            product.setId(maxId++);
            products.add(product);
        }

        writeLock.unlock();
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        products.removeIf(product -> id.equals(product.getId()));
        writeLock.unlock();
    }

    private synchronized void saveSampleProducts(){
        Currency usd = Currency.getInstance("USD");
        save(new Product("sgs", "Samsung Galaxy S", new BigDecimal(100), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S.jpg"));
        save(new Product("sgs2", "Samsung Galaxy S II", new BigDecimal(200), usd, 0, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20II.jpg"));
        save(new Product("sgs3", "Samsung Galaxy S III", new BigDecimal(300), usd, 5, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Samsung/Samsung%20Galaxy%20S%20III.jpg"));
        save(new Product("iphone", "Apple iPhone", new BigDecimal(200), usd, 10, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone.jpg"));
        save(new Product("iphone6", "Apple iPhone 6", new BigDecimal(1000), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Apple/Apple%20iPhone%206.jpg"));
        save(new Product("htces4g", "HTC EVO Shift 4G", new BigDecimal(320), usd, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg"));
        save(new Product("sec901", "Sony Ericsson C901", new BigDecimal(420), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Ericsson%20C901.jpg"));
        save(new Product("xperiaxz", "Sony Xperia XZ", new BigDecimal(120), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Sony/Sony%20Xperia%20XZ.jpg"));
        save(new Product("nokia3310", "Nokia 3310", new BigDecimal(70), usd, 100, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Nokia/Nokia%203310.jpg"));
        save(new Product("palmp", "Palm Pixi", null, usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Palm/Palm%20Pixi.jpg"));
        save(new Product("simc56", "Siemens C56", new BigDecimal(70), usd, 20, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C56.jpg"));
        save(new Product("simc61", "Siemens C61", new BigDecimal(80), usd, 30, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20C61.jpg"));
        save(new Product("simsxg75", "Siemens SXG75", new BigDecimal(150), usd, 40, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/Siemens/Siemens%20SXG75.jpg"));
    }
}
