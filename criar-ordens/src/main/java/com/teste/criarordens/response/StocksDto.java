package com.teste.criarordens.response;

import lombok.Data;

@Data
public class StocksDto {
    private Long id;
    private Double askMin;
    private Double askMax;
    private Double bidMin;
    private Double bidMax;
    private String stockSymbol;
    private String stockName;
}
