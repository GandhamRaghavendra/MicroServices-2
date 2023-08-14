package com.micro.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class BasicFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(BasicFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {

        MDC.put("userId","unique_User_Id");

        logger.info("Inside BasicFilter of AuthService..!");

        MDC.remove("userId");

        filterChain.doFilter(req, res);

    }
}
