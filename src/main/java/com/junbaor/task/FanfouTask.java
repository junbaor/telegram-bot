package com.junbaor.task;

import com.junbaor.handler.FanfouHandler;
import com.junbaor.util.AppUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Date;

@Component
public class FanfouTask {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(FanfouTask.class);
    private static final String pattern = "yyyy-MM-dd";

    @Value("${daily.chatId}")
    public Long dailyChatId;

    @Value("${weekly.chatId}")
    public Long weeklyChatId;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private FanfouHandler fanfouHandler;

    @Scheduled(cron = "0 5 8 * * *")
    public void daily() {
        long start = System.currentTimeMillis();
        log.info("每日精选启动...");

        try {
            String date = DateFormatUtils.format(new Date(), pattern);
            fanfouHandler.sendContent(dailyChatId, AppUtils.getFanFouDailyByDate(date));
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
            appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }

        log.info("每日精选完成,耗时:{} ms", System.currentTimeMillis() - start);
    }

    @Scheduled(cron = "0 5 8 * * mon")
    public void weekly() {
        long start = System.currentTimeMillis();
        log.info("每周精选启动...");

        try {
            String date = DateFormatUtils.format(new Date(), pattern);
            fanfouHandler.sendContent(weeklyChatId, AppUtils.getFanFouWeeklyByDate(date));
        } catch (TelegramApiException e) {
            log.info(e.getMessage());
            appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }

        log.info("每周精选完成,耗时:{} ms", System.currentTimeMillis() - start);
    }

}
