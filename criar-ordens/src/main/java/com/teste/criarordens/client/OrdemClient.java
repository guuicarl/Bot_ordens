package com.teste.criarordens.client;

import com.teste.criarordens.response.StocksDto;
import com.teste.criarordens.response.UserDto;
import com.teste.criarordens.response.UserOrderDto;
import com.teste.criarordens.response.UserStockDto;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class OrdemClient {
    @Autowired
    private WebClient webClienStock;
    private final WebClient webClient;
    private Random gerador = new Random();
    public OrdemClient(WebClient.Builder builder){
        webClient = builder.baseUrl("http://localhost:8082").build();
    }

    public List<UserDto> user(@RequestHeader("Authorization") String token){
        Mono<UserDto[]> monoStock = this.webClient
                .method(HttpMethod.GET)
                .uri("/users")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(UserDto[].class);
        return Arrays.stream(monoStock.block()).toList();

    }

    public List<StocksDto> stocks(@RequestHeader("Authorization") String token){
        Mono<StocksDto[]> monoStock = this.webClienStock
                .method(HttpMethod.GET)
                .uri("/stocks")
                .header(HttpHeaders.AUTHORIZATION, token)
                .retrieve()
                .bodyToMono(StocksDto[].class);
        return Arrays.stream(monoStock.block()).toList();

    }

    public UserOrderDto criarOrdem(@RequestHeader("Authorization") String token){
        int a = gerador.nextInt(user(token).size());
        int b = gerador.nextInt(stocks(token).size());
        Long volume = Long.valueOf(gerador.nextInt(1,100));
        Long idUser = user(token).get(a).getId();
        Long idStock = stocks(token).get(b).getId();
        Double price = Math.round(gerador.nextDouble(1, 100)*100.00)/100.00;
        int type = gerador.nextInt(0,2);
        if(type == 1){
            criarStock(token, idStock, idUser, stocks(token).get(b).getStockSymbol(),stocks(token).get(b).getStockName(), volume);
        }
        JSONObject json = new JSONObject();
        json.put("id_user", idUser);
        json.put("id_stock", idStock);
        json.put("stock_symbol", stocks(token).get(b).getStockSymbol());
        json.put("stock_name", stocks(token).get(b).getStockName());
        json.put("volume", volume);
        json.put("price", price);
        json.put("type", type);
        json.put("status", 1);
        json.put("remaining_value", volume);
        Mono<UserOrderDto> monoStock =
                this.webClient
                        .post()
                        .uri("/orders")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .body(BodyInserters.fromValue(json))
                        .retrieve()
                        .bodyToMono(UserOrderDto.class);
        return monoStock.block();
    }

    public UserStockDto criarStock(@RequestHeader("Authorization") String token, Long idStock, Long idUser, String stockSymbol, String stockName, Long volume){
        JSONObject json = new JSONObject();
        json.put("id_user", idUser);
        json.put("id_stock", idStock);
        json.put("stock_symbol", stockSymbol);
        json.put("stock_name", stockName);
        json.put("volume", volume);
        Mono<UserStockDto> monoStock =
                this.webClient
                        .post()
                        .uri("/")
                        .header(HttpHeaders.AUTHORIZATION, token)
                        .body(BodyInserters.fromValue(json))
                        .retrieve()
                        .bodyToMono(UserStockDto.class);
        return monoStock.block();
    }

    @PostConstruct
    public void teste() throws InterruptedException {
        for (var i = 0; i < 5; i++) {
            Thread.sleep(3000);
            String token = "Bearer eyJraWQiOiJGOUxUSktJa2pmMGVzdHFjWVgzM3lXRi1KckVPcWRReHQtb1owT01BaERvIiwiYWxnIjoiUlMyNTYifQ.eyJ2ZXIiOjEsImp0aSI6IkFULlJHc1REWGNpOVBuUW10MjJSNlZrRW1mR3dpUjVQNWFtaGJpU25oQnRkOUkiLCJpc3MiOiJodHRwczovL2Rldi0zMDcxNzg2Lm9rdGEuY29tL29hdXRoMi9kZWZhdWx0IiwiYXVkIjoiYXBpOi8vZGVmYXVsdCIsImlhdCI6MTY0NzI3ODgyNCwiZXhwIjoxNjQ3MjgyNDI0LCJjaWQiOiIwb2EzazhyZzdlYVA1TzN2YjVkNyIsInVpZCI6IjAwdTNqdWF3ZmpsU3B6dHVRNWQ3Iiwic2NwIjpbImVtYWlsIiwicHJvZmlsZSIsIm9wZW5pZCJdLCJhdXRoX3RpbWUiOjE2NDcyNzg4MjIsInN1YiI6Imd1aWxoZXJtZS5jYXJsb3MwMTRAZ21haWwuY29tIn0.H96tJhZYRtClQoolbjRbxGIPmHHYX8q7hn2CISi2FiIR3-rDyIaLfFwqaAdtaPeyyC4cu64xg32jq1ax_o9-iV4fXNzYHTZ6JuDlRm8SDBJrfFdpRcOJrgtOGDZMP2jsvtswuy4os-fe7ak9OELGzgb0dS2slbjX0aDV6v5eYh4wepuPKAqwfRYIwalSlau9E-a34Z1I0tRB11t-nXR3caiVF8Nnr96JS9FQsyUdOqMHd_F9NrHK6JRzNVGGIzsAMocXKfZTBmDEUg6zt5aQQELevySfFKUhderC1uCZ5eCxwK7bpBuyvcpCP46ln5WEGO26qfUrIT6VGqI1E_LZmg";
            criarOrdem(token);
        }

    }

}
