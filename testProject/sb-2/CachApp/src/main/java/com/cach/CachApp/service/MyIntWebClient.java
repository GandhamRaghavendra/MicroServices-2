package com.cach.CachApp.service;

import com.cach.CachApp.model.ApiResponse;
import com.cach.CachApp.util.ApiConstants;
import com.cach.CachApp.util.ParseJsonClass;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import lombok.extern.slf4j.Slf4j;
//import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Component
@Slf4j
public class MyIntWebClient {

    private final WebClient webClient;

    @Autowired
    private ParseJsonClass parseJsonClass;

    public MyIntWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(ApiConstants.BASE_URL_INT).build();
    }

    public ApiResponse webClintGenericCall(String key, String intEndpoints) {

        Object response = webClient.get()
                .uri(intEndpoints)
                .header("api-key", key)
                .retrieve()
                .bodyToMono(Object.class)
                .block();

        log.info("WebClintCall made for: {}",
                intEndpoints);

        return new ApiResponse(response);

    }
}
