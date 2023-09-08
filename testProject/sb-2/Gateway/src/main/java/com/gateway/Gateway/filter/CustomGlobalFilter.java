package com.gateway.Gateway.filter;

import com.gateway.Gateway.util.EndpointMapProperties;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.nonNull;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private EndpointMapProperties  endpointMapProperties;

    @Autowired
    private RouteValidator routeValidator;

    private String cacheKey;

    private CachedRequest cachedRequest;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        cachedRequest = getCachedRequest(exchange.getRequest());

        log.info("Inside CustomGlobalFilter()..!");

        log.info("RequestPath: "+cachedRequest.getPath()+" RequestMethod: "+cachedRequest.getMethod());


        // here we test the rout is cached or not.
        if(routeValidator.isCached.test(exchange.getRequest())){

            log.info("This is a '/fno' rout..");

            Cache cache = getCacheByRout(cachedRequest.getPath().toString());

            cacheKey = generateCacheKey(exchange.getRequest());

            log.info("CacheKey :"+cacheKey);

            if (nonNull(cache.get(cacheKey))) {

                log.info("Return cached response for request: {}", cacheKey);

                CachedResponse cachedResponse = cache.get(cacheKey, CachedResponse.class);

                ServerHttpResponse serverHttpResponse = exchange.getResponse();

                serverHttpResponse.setStatusCode(cachedResponse.httpStatus);

                serverHttpResponse.getHeaders().addAll(cachedResponse.headers);

                DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(cachedResponse.body);

                return exchange.getResponse().writeWith(Flux.just(buffer));
            }

            ServerHttpResponse mutatedHttpResponse = getServerHttpResponse(exchange, cache, cachedRequest);

            return chain.filter(exchange.mutate().response(mutatedHttpResponse).build());
        }
        else {
            log.info("This rout is not '/fno' rout");

            return chain.filter(exchange);
        }
    }

    private ServerHttpResponse getServerHttpResponse(ServerWebExchange exchange, Cache cache, CachedRequest cachedRequest) {

        log.info("Inside getServerHttpResponse() method");

        ServerHttpResponse originalResponse = exchange.getResponse();

        DataBufferFactory dataBufferFactory = originalResponse.bufferFactory();

        return new ServerHttpResponseDecorator(originalResponse) {

            @NonNull
            @Override
            public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {

                if (body instanceof Flux) {

                    Flux<? extends DataBuffer> flux = (Flux<? extends DataBuffer>) body;

                    return super.writeWith(flux.buffer().map(dataBuffers -> {

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                        dataBuffers.forEach(dataBuffer -> {

                            byte[] responseContent = new byte[dataBuffer.readableByteCount()];

                            dataBuffer.read(responseContent);

                            try {
                                outputStream.write(responseContent);
                            } catch (IOException e) {
                                throw new RuntimeException("Error while reading response stream", e);
                            }

                        });

                        // here we will only cache the success response of /fno routes only.
                        if (Objects.requireNonNull(getStatusCode()).is2xxSuccessful() && routeValidator.isCached.test(exchange.getRequest())) {

                            CachedResponse cachedResponse = new CachedResponse(getStatusCode(), getHeaders(), outputStream.toByteArray());

                            log.debug("Request {} Cached response {}", cacheKey, new String(cachedResponse.getBody(), UTF_8));

                            cache.put(cacheKey, cachedResponse);
                        }
                        return dataBufferFactory.wrap(outputStream.toByteArray());
                    }));
                }
                return super.writeWith(body);
            }
        };
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private CachedRequest getCachedRequest(ServerHttpRequest request) {
        return CachedRequest.builder().method(request.getMethod()).path(request.getPath()).build();
    }

    @Value
    @Builder
    private static class CachedRequest {
        RequestPath path;
        HttpMethod method;
    }

    @Value
    private static class CachedResponse {
        HttpStatusCode httpStatus;
        HttpHeaders headers;
        byte[] body;
    }

    private String generateCacheKey(ServerHttpRequest request) {
        return request.getURI().getRawPath();
    }

    private Cache getCacheByRout(String rout){

        Map<String, Integer> endpoints = endpointMapProperties.getEndpoints();

        log.info("Inside getCacheByRout()..");

        log.debug("endpoints Map: "+endpoints.toString());

        rout = rout.substring(5);

        log.debug("Rout: "+ rout);

        if(endpoints.get(rout) <= 10){
            Cache cache = cacheManager.getCache("sortCache");

            log.debug("sortCache Assigned to this rout: "+rout);

            return cache;
        }

        log.debug("longCache Assigned to this rout: "+rout);

        return cacheManager.getCache("longCache");
    }

}