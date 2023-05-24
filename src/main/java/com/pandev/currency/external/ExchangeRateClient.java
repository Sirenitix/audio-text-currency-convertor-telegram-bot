package com.pandev.currency.external;

import com.pandev.currency.external.entity.ExchangeResponse;
import com.pandev.currency.external.exception.ExchangeFailure;
import java.math.BigDecimal;
import javax.money.CurrencyUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExchangeRateClient {

    @Value("${exchange-rate-api.base-url}")
    String baseUrl;

    @Value("${exchange-rate-api.api-key}")
    String apiKey;

    private final WebClient webClient;

    public BigDecimal getExchangeRate(CurrencyUnit from, CurrencyUnit to) {
        log.info(from.getCurrencyCode() + " - from");
        log.info(to.getCurrencyCode()  + " - to");
        return webClient.get()
            .uri(baseUrl + "/v6/{apiKey}/pair/{from}/{to}", apiKey, from, to)
            .retrieve()
            .bodyToMono(ExchangeResponse.class)
            .blockOptional()
            .map(response -> {
                log.info(response + " - exchange rate response");
                if ("error".equals(response.getResult())) {
                    throw new ExchangeFailure();
                } else {
                    return response.getConversionRate();
                }
            })
            .orElseThrow(ExchangeFailure::new);
    }

}
