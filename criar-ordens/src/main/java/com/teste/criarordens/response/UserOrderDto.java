package com.teste.criarordens.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

@Data
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class UserOrderDto {
    private Long idUser;
    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
    private Double price;
    private Integer type;
    private Integer status;
    private Long remainingValue;
}
