package com.junbaor.handler;

import com.junbaor.util.AppUtils;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.Map;

@Component
public class YiYanHandler extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(YiYanHandler.class);

    @Value("${yiyan.bot.token}")
    private String botToken;

    @Value("${yiyan.bot.token}")
    private String botName;

    @Autowired
    private AppUtils appUtils;

    private static Map<String, String> type = new HashMap<>();

    static {
        type.put("/Anime", "a");
        type.put("/Comic", "b");
        type.put("/Game", "c");
        type.put("/Novel", "d");
        type.put("/Myself", "e");
        type.put("/Internet", "f");
        type.put("/Other", "g");
        type.put("/Random", "random");
    }

    public YiYanHandler(@Autowired DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            if (update.getMessage() != null) {
                Message message = update.getMessage();
                Long chatId = message.getChatId();

                String text = message.getText();
                String response = response(text);
                log.info("响应结果：{}", response);

                SendMessage sendMessage = new SendMessage();
                sendMessage.setChatId(chatId);
                sendMessage.setText(response);
                sendMessage(sendMessage);
            }
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    public String response(String str) {
        if ("/start".equals(str)) {
            return "你可以向我发送以下命令:\n\n" +
                    "/anime - 动画\n" +
                    "/comic - 漫画\n" +
                    "/game - 游戏\n" +
                    "/novel - 小说\n" +
                    "/myself - 原创\n" +
                    "/internet - 来自网络\n" +
                    "/other - 其他\n" +
                    "/random - 随机";
        }

        String typeParams = type.get(str);
        if (typeParams == null) {
            typeParams = "random";
        }

        try {
            String yiyan = Requests.get("https://sslapi.hitokoto.cn/?encode=text&c=" + typeParams).send().readToText();
            return yiyan;
        } catch (Exception e) {
            appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
            return "嗯? 有点不对劲哎~~~";
        }

    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
