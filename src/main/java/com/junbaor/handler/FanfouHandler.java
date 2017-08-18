package com.junbaor.handler;

import com.junbaor.model.ResponsePo;
import com.junbaor.util.AppUtils;
import net.dongliu.requests.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FanfouHandler extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(FanfouHandler.class);

    public FanfouHandler(DefaultBotOptions options) {
        super(options);
    }

    public void sendContent(Long chatId, ResponsePo fromJson) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        List<ResponsePo.MsgsBean> msgs = fromJson.getMsgs();
        for (ResponsePo.MsgsBean msg : msgs) {
            sendMessage.setText(AppUtils.filter(msg.getMsg()));

            InlineKeyboardButton buttonOne = new InlineKeyboardButton();
            buttonOne.setText(msg.getRealname());
            buttonOne.setUrl("http://fanfou.com/" + msg.getLoginname());

            InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
            buttonTwo.setText("原文");
            buttonTwo.setUrl("http://fanfou.com/statuses/" + msg.getStatusid());

            List<InlineKeyboardButton> objects = Arrays.asList(buttonOne, buttonTwo);

            sendMessage.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(Arrays.asList(objects)));
            sendMessage(sendMessage);

            ResponsePo.MsgsBean.ImgBean imgs = msg.getImg();
            if (!imgs.getPreview().equals("")) {
                SendPhoto sendPhoto = new SendPhoto();
                sendPhoto.setChatId(chatId);

                String fileRoot = AppUtils.getFilePath();

                File file = new File(fileRoot + msg.getId());
                Requests.get(AppUtils.convert(imgs.getPreview())).send().writeToFile(file);
                sendPhoto.setNewPhoto(file);
                sendPhoto(sendPhoto);
            }
        }
    }

    @Override
    public String getBotToken() {
        return System.getProperty("bot.token");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getChannelPost() != null) {
            log.info("chatId:{} text:{}", update.getChannelPost().getChatId(), update.getChannelPost().getText());
        } else {
            log.info("chatId:{} text:{}", update.getMessage().getChatId(), update.getMessage().getText());
        }
    }

    @Override
    public String getBotUsername() {
        return System.getProperty("bot.name");
    }

}
