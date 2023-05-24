package com.pandev.currency.service;

import com.pandev.currency.listener.CustomListener;
import com.pengrad.telegrambot.TelegramBot;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyInit {

    @Value("${telegram-bot.token}")
    private String botToken;

    @Getter
    private TelegramBot telegramBot;

    private final CustomListener customListener;

    @PostConstruct
    private void setup(){
        telegramBot = new TelegramBot(botToken);
        telegramBot.setUpdatesListener(customListener);
    }
}


