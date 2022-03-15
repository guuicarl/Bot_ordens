package com.teste.criarordens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
public class CriarOrdensApplication {

	public static void main(String[] args) {
		SpringApplication.run(CriarOrdensApplication.class, args);
	}

	@Bean
	public WebClient webClienStock(WebClient.Builder builder) {
		return builder
				.baseUrl("http://localhost:8085")
						.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
						.build();
	}


}
