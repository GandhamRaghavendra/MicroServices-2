package com.micro.filter;

import jakarta.servlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class BasicFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(BasicFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        logger.info("Inside BasicFilter of AuthService..!");

    }
}
