package com.junbaor;

import com.junbaor.handler.FanfouHandler;
import com.junbaor.task.DailyTask;
import com.junbaor.task.WeeklyTask;
import com.junbaor.util.AppUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.junbaor.util.AppUtils.getConfig;
import static com.junbaor.util.AppUtils.initConfig;

/**
 * Hello world!
 */
public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    private static Long dailyChatId;
    private static Long weeklyChatId;

    public static void main(String[] args) {
        initConfig();
        initChatId();

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();

        DefaultBotOptions botOptions = new DefaultBotOptions();
        if (AppUtils.isWindowsOS()) {
            HttpHost httpHost = new HttpHost("127.0.0.1", 1080, "http");
            RequestConfig requestConfig = RequestConfig.custom().setProxy(httpHost).build();
            botOptions.setRequestConfig(requestConfig);
        }

        FanfouHandler fanfouHandler = new FanfouHandler(botOptions);

        try {
            telegramBotsApi.registerBot(fanfouHandler);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }

        DailyTask dailyTask = new DailyTask(fanfouHandler, dailyChatId);
        WeeklyTask weeklyTask = new WeeklyTask(fanfouHandler, weeklyChatId);

        Long nextTime = AppUtils.getNextTime();
        log.info("距离下次任务还有 {} ms", nextTime);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(2);
        executorService.scheduleAtFixedRate(dailyTask, nextTime, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
        executorService.scheduleAtFixedRate(weeklyTask, nextTime, 24 * 60 * 60 * 1000, TimeUnit.MILLISECONDS);
    }

    public static void initChatId() {
        dailyChatId = new Long(getConfig("daily.chatId"));
        weeklyChatId = new Long(getConfig("weekly.chatId"));
    }
}
