package com.junbaor.task;

import com.junbaor.handler.FanfouHandler;
import com.junbaor.model.ResponsePo;
import com.junbaor.util.AppUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class DailyTask implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(DailyTask.class);
    private FanfouHandler fanfouHandler;
    private Long charId;

    public DailyTask(FanfouHandler fanfouHandler, Long charId) {
        this.fanfouHandler = fanfouHandler;
        this.charId = charId;
    }

    @Override
    public void run() {
        log.info("每天任务启动...");
        try {
            ResponsePo fanFouDaily = AppUtils.getFanFouDaily();
            fanfouHandler.sendContent(charId, fanFouDaily);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
