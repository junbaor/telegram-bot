package com.junbaor.task;

import com.junbaor.handler.FanfouHandler;
import com.junbaor.model.ResponsePo;
import com.junbaor.util.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class WeeklyTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DailyTask.class);
    private FanfouHandler fanfouHandler;
    private Long charId;

    public WeeklyTask(FanfouHandler fanfouHandler, Long charId) {
        this.fanfouHandler = fanfouHandler;
        this.charId = charId;
    }

    @Override
    public void run() {
        log.info("每周任务启动...");
        try {
            if (AppUtils.getDayOnWeek() == 1) {
                ResponsePo fanFouWeekly = AppUtils.getFanFouWeekly();
                fanfouHandler.sendContent(charId, fanFouWeekly);
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }
}
