package com.cach.CachApp.controller;

import com.cach.CachApp.model.ApiResponse;
import com.cach.CachApp.model.MktData;
import com.cach.CachApp.service.TestService;
import com.cach.CachApp.util.IntEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fno")
public class FNOController {

    @Autowired
    private TestService testService;

    @GetMapping
    public ResponseEntity<String> check(@RequestHeader("api-key") String key){
        // Now you can use the 'apiKey' variable to access the value of the "api-key" header
        if ("futZ90Ky-jm".equals(key)) {
            return new ResponseEntity<>("Authorized", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Unauthorized", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/techsignal/RELIANCE/5")
    public ResponseEntity<ApiResponse> test(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.Technical_Signal);

//        ApiResponse res = testService.testMethod(key);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/multistrikeOi/NIFTY")
    public ResponseEntity<ApiResponse> method2(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.MultiStrikeOi);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/combinedOi/NIFTY")
    public ResponseEntity<ApiResponse> method3(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.CombinedOi);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/futuresDashboard/NEAR")
    public ResponseEntity<ApiResponse> method4(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.Futures_Dashboard);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }


    @GetMapping("/momentum-score/N")
    public ResponseEntity<ApiResponse> method5(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.Momentum_Stocks);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/reloutperf")
    public ResponseEntity<ApiResponse> method6(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.Relative_Outperformance);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @GetMapping("/volume/HIGH_DELIVERY_PERCENTAGE")
    public ResponseEntity<ApiResponse> method7(@RequestHeader("api-key") String key){

        ApiResponse res = testService.restCallGeneric(key, IntEndpoints.Volume_Delivery_Scans);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

}
