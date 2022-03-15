package com.teste.criarordens.response;

import lombok.Data;

@Data
public class UserStockDto {
    private Long idUser;
    private Long idStock;
    private String stockSymbol;
    private String stockName;
    private Long volume;
}
