package com.es.phoneshop.model;

import com.es.phoneshop.model.security.DefaultDosProtectionService;
import com.es.phoneshop.model.security.DosProtectionService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DefaultDosProtectionServiceTest {
    private static DosProtectionService dosProtectionService;
    @Before
    public void setup() {
        dosProtectionService = DefaultDosProtectionService.getInstance();
    }

    @Test
    public void testAllowedStateBeforeMinute() {
        String ip = "good-ip";
        assertTrue(dosProtectionService.isAllowed(ip));
    }

    @Test
    public void testNotAllowedState() throws InterruptedException {
        String ip = "bad-ip";
        for(int i = 0; i < 200; i++) {
            dosProtectionService.isAllowed(ip);
        }

        assertFalse(dosProtectionService.isAllowed(ip));
    }
}
