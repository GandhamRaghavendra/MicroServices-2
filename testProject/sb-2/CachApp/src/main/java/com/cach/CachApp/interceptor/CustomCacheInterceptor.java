package com.cach.CachApp.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class CustomCacheInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("Inside preHandle() CustomCacheInterceptor..!");

        log.info(request.getPathInfo());

        // Return true to continue with the execution chain.
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("Inside postHandle() CustomCacheInterceptor..!");

        log.info("Response Status: "+response.getStatus());

        if (response instanceof ContentCachingResponseWrapper && response.getStatus() == 200) {
            ContentCachingResponseWrapper responseWrapper = (ContentCachingResponseWrapper) response;
            byte[] responseBody = responseWrapper.getContentAsByteArray();
            if (responseBody.length > 0) {
                String responseBodyString = new String(responseBody, response.getCharacterEncoding());
                log.info("Response Body: " + responseBodyString);
            }
        }

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // This method is called after the view is rendered.
        // You can perform cleanup tasks here.
        log.info("Inside afterCompletion() CustomCacheInterceptor..!");
    }

}
