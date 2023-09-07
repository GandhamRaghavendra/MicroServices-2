package com.cach.CachApp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubData {

    private String overall;

    private int bullish;

    private int neutral;

    private int bearish;
}
