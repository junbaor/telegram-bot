package com.junbaor;

import com.junbaor.handler.FanfouHandler;
import com.junbaor.handler.GetIdHandler;
import com.junbaor.util.AppUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;

@EnableScheduling
@SpringBootApplication
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    @Autowired
    private FanfouHandler fanfouHandler;
    @Autowired
    private GetIdHandler getIdHandler;
    @Autowired
    private AppUtils appUtils;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @PostConstruct
    public void initBot() {
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        try {
            telegramBotsApi.registerBot(fanfouHandler);
            telegramBotsApi.registerBot(getIdHandler);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }


}
