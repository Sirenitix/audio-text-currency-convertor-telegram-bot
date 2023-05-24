package com.pandev.currency.listener;

import com.pandev.currency.external.ExchangeRateClient;
import com.pandev.currency.helper.CurrencyHelper;
import com.pandev.currency.service.CurrencyInit;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.math.BigDecimal;
import java.util.List;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomListener implements UpdatesListener {

    private final CurrencyInit currencyInit;

    private final ExchangeRateClient rateClient;

    @Autowired
    public CustomListener(@Lazy CurrencyInit currencyInit, ExchangeRateClient rateClient) {
        this.currencyInit = currencyInit;
        this.rateClient = rateClient;
    }

    @Override
    public int process(List<Update> updates) {
        log.info("Listener running");
        log.info("Updates size: {}", updates.size());
        for (Update update : updates) {
            String message = update.message().text();
            if (!CurrencyHelper.guard(message)) {
                continue;
            }
            String[] currencies = chooseConversionType(message);
            String result = getResult(CurrencyHelper.getAmount(message), currencies);
            sendMessageBackToUser(update, result);
        }
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

    private void sendMessageBackToUser(Update update, String result) {
        SendMessage request = new SendMessage(update.message().chat().id(), result);
        SendResponse sendResponse = currencyInit.getTelegramBot().execute(request);
        boolean ok = sendResponse.isOk();
        Message message = sendResponse.message();
        log.info("Response status ok and message: {}, {}", ok, message.toString());
    }

    private String[] chooseConversionType(String message) {
        String currency = CurrencyHelper.getCurrency(message);
        return currency.equals("USD")
            ? new String[] {"KZT", "USD"}
            : new String[] {"USD", "KZT"};
    }

    private String getResult(int amount, String[] currencies) {
        MonetaryAmount fstAmtUSD = Monetary.getDefaultAmountFactory()
            .setCurrency(currencies[1]).setNumber(amount).create();
        CurrencyUnit currency = fstAmtUSD.getCurrency();
        return rateClient.getExchangeRate(currency,
                Monetary.getCurrency(currencies[0]))
            .multiply(BigDecimal.valueOf(amount))
            .toString();
    }

}
