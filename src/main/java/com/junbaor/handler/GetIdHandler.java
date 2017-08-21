package com.junbaor.handler;

import com.junbaor.util.AppUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

@Component
public class GetIdHandler extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(GetIdHandler.class);

    @Autowired
    private AppUtils appUtils;
    @Value("${getid.bot.token}")
    private String botToken;

    @Value("${getid.bot.token}")
    private String botName;

    public GetIdHandler(@Autowired DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null) {
            Long chatId = update.getMessage().getChatId();
            String text = update.getMessage().getText();

            if ("/start".equals(text)) {
                send(chatId, "Hello, You can get an id for sending or forwarding messages");
            }

            send(chatId, "Your telegram id: " + chatId);

            Chat forwardFromChat = update.getMessage().getForwardFromChat();
            if (forwardFromChat != null) {
                send(chatId, "Message source id: " + update.getMessage().getForwardFromChat().getId());
            }
        }
    }

    private void send(Long chatId, String string) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(string);
            sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
            appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
        }
    }

    @Override
    public String getBotUsername() {
        return botToken;
    }

    @Override
    public String getBotToken() {
        return botName;
    }

}
