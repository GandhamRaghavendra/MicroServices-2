package com.gateway.Gateway.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MktData {

    private String symbol;

    private SubData netStrength;

    private SubData mastrength;

    private SubData osstrength;

    private SubData costrength;
}
