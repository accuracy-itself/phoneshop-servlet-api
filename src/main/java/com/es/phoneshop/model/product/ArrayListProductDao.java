package com.es.phoneshop.model.product;

import java.util.*;
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
    private static volatile ProductDao instance;

    private ArrayListProductDao() {
        this.products = new ArrayList<>();
    }

    public static ProductDao getInstance() {
        ProductDao localInstance = instance;
        if (localInstance == null) {
            synchronized (ArrayListProductDao.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ArrayListProductDao();
                }
            }
        }
        return localInstance;
    }

    @Override
    public Product getProduct(Long id) throws ProductNotFoundException {
        Product productFound;

        readLock.lock();
        try {
            productFound = products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .findAny()
                    .orElseThrow(() -> new ProductNotFoundException(id));
        } finally {
            readLock.unlock();
        }

        return productFound;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        try {
            Comparator<Product> comparator = getComparator(query, sortField);

            return products.stream()
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> query == null || query.equals("") || Arrays.stream(query.split(" "))
                            .anyMatch(word -> product.getDescription().contains(word)))
                    .sorted((sortOrder == SortOrder.DESC) ? comparator.reversed() : comparator)
                    .collect(Collectors.toList());

        } finally {
            readLock.unlock();
        }
    }

    private Comparator<Product> getComparator(String query, SortField sortField) {
        Map<SortField, Comparator<Product>> comparatorMap = new HashMap<>();
        comparatorMap.put(SortField.DESCRIPTION, Comparator.comparing(Product::getDescription));
        comparatorMap.put(SortField.PRICE, Comparator.comparing(Product::getPrice));
        return comparatorMap.getOrDefault(sortField, (query != null && !query.equals(""))
                ? Comparator.comparing(product -> (Comparable) (Arrays.stream(query.split(" "))
                .filter(word -> ((Product) product).getDescription().contains(word)).count())).reversed()
                : Comparator.comparing(Product::getId));
    }

    @Override
    public void save(Product product) {
        Long id = product.getId();

        writeLock.lock();

        if (id != null) {
            Optional<Product> productFound = products.stream()
                    .filter(prod -> id.equals(prod.getId()))
                    .findAny();

            if (productFound.isPresent()) {
                products.set(products.indexOf(productFound.get()), product);
            } else {
                products.add(product);
            }
        } else {
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
