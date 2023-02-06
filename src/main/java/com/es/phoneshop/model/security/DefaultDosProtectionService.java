package com.es.phoneshop.model.security;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultDosProtectionService implements DosProtectionService {
    private static volatile DefaultDosProtectionService instance;
    private static final long THRESHOLD = 100;
    private Map<String, Long> countMap = new ConcurrentHashMap<>();
    private Map<String, LocalDateTime> timerMap = new ConcurrentHashMap<>();

    private DefaultDosProtectionService() {
    }

    public static DefaultDosProtectionService getInstance() {
        DefaultDosProtectionService localInstance = instance;
        if (localInstance == null) {
            synchronized (DefaultDosProtectionService.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DefaultDosProtectionService();
                }
            }
        }
        return localInstance;
    }

    @Override
    public boolean isAllowed(String ip) {
        Long count = countMap.get(ip);
        if (count == null) {
            count = 1L;
            countMap.put(ip, count);
            timerMap.put(ip, LocalDateTime.now());
        } else {
            if (LocalDateTime.now().minusMinutes(1).isBefore(timerMap.get(ip))) {
                if (count > THRESHOLD) {
                    return false;
                }
                countMap.put(ip, count + 1);
                return true;
            } else {
                countMap.put(ip, 1L);
                timerMap.put(ip, LocalDateTime.now());
            }
        }

        return true;
    }
}
