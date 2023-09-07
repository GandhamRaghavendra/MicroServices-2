package com.micro.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Component
@Order(1)
public class ProductBasicFilter implements Filter {


    private final Logger logger = LoggerFactory.getLogger(ProductBasicFilter.class);
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        logger.info("Inside ProductBasicFilter of (PRODUCTSERVICE)..");

        HttpServletRequest request = (HttpServletRequest) req;

        try {
            String requestId = request.getHeader("RequestId");

            String key = request.getHeader("key");

            if (StringUtils.hasText(requestId)) {

                MDC.put("RequestId", requestId);

            }
            if (StringUtils.hasText(key)) {

                MDC.put("key", key);

            }

            logger.info(MDC.get("key")+MDC.get("RequestId"));

        } catch (Exception e) {
            logger.debug("Missing Request Headers..");
            logger.error(e.getMessage());
        }

        filterChain.doFilter(req, res);

    }

    @Override
    public void destroy() {
        MDC.clear();
    }
}
