package com.es.phoneshop.web;

import javax.servlet.http.HttpServletRequest;

public class IdParser {
    public static Long parseProductId(HttpServletRequest request) {
        String productId = request.getPathInfo();
        return Long.valueOf(productId.substring(1));
    }
}
