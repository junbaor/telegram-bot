package com.junbaor.handler;

import com.junbaor.model.EchoResponsePo;
import com.junbaor.util.AppUtils;
import net.dongliu.requests.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.api.methods.send.SendAudio;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class EchoHandler extends TelegramLongPollingBot {

    private static final Logger log = LoggerFactory.getLogger(EchoHandler.class);

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Value("${echo.bot.token}")
    private String botToken;

    @Value("${echo.bot.name}")
    private String botName;

    public EchoHandler(@Autowired DefaultBotOptions options) {
        super(options);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() == null) {
            return;
        }
        executorService.submit(new EchoDownload(update));
    }

    class EchoDownload implements Runnable {

        private Update update;

        EchoDownload(Update update) {
            this.update = update;
        }

        @Override
        public void run() {
            try {
                log.info("解析地址:{}", update.getMessage().getText());

                Long chatId = update.getMessage().getChatId();
                Integer messageId = update.getMessage().getMessageId();
                String text = update.getMessage().getText();

                if (text != null && text.indexOf("app-echo.com") < 0) {
                    return;
                }

                String musicId = text.replaceAll("http.?://www.app-echo.com/#/sound/", "").replaceAll("http.?://www.app-echo.com/sound/info\\?sound_id=", "");

                String jsonString = Requests.get("http://www.app-echo.com/api/sound/info?id=" + musicId).send().readToText();
                EchoResponsePo responsePo = AppUtils.gson.fromJson(jsonString, EchoResponsePo.class);
                if (responsePo.getStatus() != 100) {
                    return;
                }

                String fileName = responsePo.getInfo().getName();
                String filePath = System.currentTimeMillis() + "-" + fileName;
                System.out.println(filePath);

                File file = new File(AppUtils.getFilePath() + filePath);
                Requests.get(responsePo.getInfo().getSource()).send().writeToFile(file);

                SendAudio document = new SendAudio();
                document.setChatId(chatId);
                document.setReplyToMessageId(messageId);
                document.setTitle(fileName);

                EchoResponsePo.InfoBean.SongInfoBean songInfo = responsePo.getInfo().getSong_info();
                if (songInfo != null && songInfo.getAuthor() != null) {
                    document.setPerformer(songInfo.getAuthor().getName());
                }

                document.setNewAudio(file);
                sendAudio(document);

                log.info("解析成功,echoId:{} echoName:{}", musicId, fileName);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
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
