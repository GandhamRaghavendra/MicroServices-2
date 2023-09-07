package com.cach.CachApp.conf;

import com.cach.CachApp.interceptor.CustomCacheInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Register the CustomInterceptor to be applied to specific routes.
        registry.addInterceptor(new CustomCacheInterceptor())
                .addPathPatterns("/fno/**");
    }
}
