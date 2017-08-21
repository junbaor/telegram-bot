package com.junbaor.handler;

import com.junbaor.model.ResponsePo;
import com.junbaor.util.AppUtils;
import net.dongliu.requests.Requests;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendDocument;
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

@Component
public class FanfouHandler extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(FanfouHandler.class);

    @Autowired
    private AppUtils appUtils;

    @Value("${bot.token}")
    private String botToken;

    @Value("${bot.token}")
    private String botName;

    public FanfouHandler(@Autowired DefaultBotOptions options) {
        super(options);
    }

    public void sendContent(Long chatId, ResponsePo fromJson) throws TelegramApiException {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);

        List<ResponsePo.MsgsBean> msgs = fromJson.getMsgs();
        for (ResponsePo.MsgsBean msg : msgs) {
            try {
                message.setText(AppUtils.filter(msg.getMsg()));

                InlineKeyboardButton buttonOne = new InlineKeyboardButton();
                buttonOne.setText(msg.getRealname());
                buttonOne.setUrl("http://fanfou.com/" + msg.getLoginname());

                InlineKeyboardButton buttonTwo = new InlineKeyboardButton();
                buttonTwo.setText("原文");
                buttonTwo.setUrl("http://fanfou.com/statuses/" + msg.getStatusid());

                List<InlineKeyboardButton> objects = Arrays.asList(buttonOne, buttonTwo);

                message.setReplyMarkup(new InlineKeyboardMarkup().setKeyboard(Arrays.asList(objects)));
                sendMessage(message);

                ResponsePo.MsgsBean.ImgBean imgs = msg.getImg();
                if (!imgs.getPreview().equals("")) {
                    String fileRoot = AppUtils.getFilePath();
                    File file = new File(fileRoot + msg.getId());
                    Requests.get(AppUtils.convert(imgs.getPreview())).send().writeToFile(file);

                    try {
                        SendPhoto photo = new SendPhoto();
                        photo.setChatId(chatId);
                        photo.setNewPhoto(file);
                        sendPhoto(photo);
                    } catch (TelegramApiException e) {
                        log.info("图片发送失败,转用文件方式发送");
                        SendDocument document = new SendDocument();
                        document.setChatId(chatId);
                        document.setNewDocument(file);
                        sendDocument(document);

                        appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
                    }
                }
            } catch (TelegramApiException e) {
                log.error(e.getMessage(), e);
                appUtils.sendServerChan(e.getMessage(), ExceptionUtils.getStackTrace(e));
            }
        }
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
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

}
