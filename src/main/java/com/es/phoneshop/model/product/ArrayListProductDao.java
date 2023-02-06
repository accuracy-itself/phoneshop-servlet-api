package com.es.phoneshop.model.product;

import com.es.phoneshop.model.ArrayListEntityDao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class ArrayListProductDao extends ArrayListEntityDao<Product> implements ProductDao {
    private static volatile ProductDao instance;
    private Map<SortField, Comparator<Product>> comparatorMap;

    private ArrayListProductDao() {
        super();
        this.entities = new ArrayList<Product>();
        maxId = new AtomicLong(0);
        comparatorMap = new HashMap<>();
        comparatorMap.put(SortField.DESCRIPTION, Comparator.comparing(Product::getDescription));
        comparatorMap.put(SortField.PRICE, Comparator.comparing(Product::getPrice));
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
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        readLock.lock();
        try {
            Comparator<Product> comparator = getComparator(query, sortField, sortOrder);

            return entities.stream()
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> query == null || query.equals("") || Arrays.stream(query.split(" "))
                            .anyMatch(word -> product.getDescription().contains(word)))
                    .sorted(comparator)
                    .collect(Collectors.toList());

        } finally {
            readLock.unlock();
        }
    }

    @Override
    public List<Product> findProductsAdvanced(String query, Boolean wordAllMatch, SortField sortField, SortOrder sortOrder,
                                              BigDecimal minPrice, BigDecimal maxPrice) {
        readLock.lock();
        try {
            List<Product> productsFound = findProducts(query, sortField, sortOrder);
            if (Boolean.TRUE.equals(wordAllMatch)) {
                String[] words = query.split(" ");
                productsFound = productsFound.stream()
                        .filter(product ->
                        {
                            for(String word : words) {
                                if (!product.getDescription().contains(word)) {
                                    return false;
                                }
                            }
                            return true;
                        }
                        )
                        .collect(Collectors.toList());
            }

            return productsFound.stream()
                    .filter(product -> minPrice == null || product.getPrice().compareTo(minPrice) >= 0)
                    .filter(product -> maxPrice == null || product.getPrice().compareTo(maxPrice) <= 0)
                    .collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    private Comparator<Product> getComparator(String query, SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = comparatorMap.getOrDefault(sortField, (query != null && !query.equals(""))
                ? getWordMatchComparator(query)
                : Comparator.comparing(Product::getId));

        return (sortOrder == SortOrder.DESC) ? comparator.reversed() : comparator;
    }

    private Comparator<Product> getWordMatchComparator(String query) {
        return Comparator.comparing(product -> (Comparable) (Arrays.stream(query.split(" "))
                .filter(word -> ((Product) product).getDescription().contains(word)).count())).reversed();
    }

    @Override
    public void delete(Long id) {
        writeLock.lock();
        entities.removeIf(product -> id.equals(product.getId()));
        writeLock.unlock();
    }

}
