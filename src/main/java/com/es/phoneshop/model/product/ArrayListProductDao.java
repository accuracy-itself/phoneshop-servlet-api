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
    private static ProductDao instance;

    public static synchronized ProductDao getInstance(){
        if(instance == null)
            instance = new ArrayListProductDao();

        return instance;
    }

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
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
                .orElseThrow(() -> new ProductNotFoundException(id));
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

}
