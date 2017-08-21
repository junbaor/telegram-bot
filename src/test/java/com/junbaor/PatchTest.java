package com.junbaor;

import com.junbaor.handler.FanfouHandler;
import com.junbaor.util.AppUtils;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class PatchTest extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(PatchTest.class);

    @Autowired
    private FanfouHandler fanfouHandler;

    @Autowired
    private AppUtils appUtils;

    @Value("${daily.chatId}")
    public Long dailyChatId;

    @Value("${weekly.chatId}")
    public Long weeklyChatId;

    @Test
    public void patch() throws TelegramApiException {
//        ResponsePo fanFouDailyByDate = AppUtils.getFanFouDailyByDate("2017-08-22");
//        fanFouDailyByDate.getMsgs().remove(0);
//        fanfouHandler.sendContent(dailyChatId, fanFouDailyByDate);
//        fanfouHandler.sendContent(dailyChatId, AppUtils.getFanFouDailyByDate("2017-08-23"));
//
//        fanfouHandler.sendContent(weeklyChatId, AppUtils.getFanFouWeeklyByDate("2017-08-21"));
    }
}
