package com.junbaor.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.junbaor.model.ResponsePo;
import net.dongliu.requests.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@Component
public class AppUtils {

    private static final Logger log = LoggerFactory.getLogger(AppUtils.class);

    @Value("${serverchan.sckey}")
    public String serverChanSckey;

    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    public static void main(String[] args) {
        System.out.println(convert("http://photo1.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0e/dp/rx_515248.jpg@596w_1l.jpg"));

        String str = "你的学问大别人未必知道，但你胸一大别人就看出来了。" +
                "转@<a href=\"http://fanfou.com/~XzdXX_fS7ZA\" class=\"former\">Tina罗</a> " +
                "转@<a href=\"http://fanfou.com/xiongmao17\" class=\"former\">洝洝</a> " +
                "转@<a href=\"http://fanfou.com/~vLuOcXigkMQ\" class=\"former\">贾星星</a> " +
                "转@<a href=\"http://fanfou.com/fyjdyl\" class=\"former\">饭友经典语录</a> " +
                "转@<a href=\"http://fanfou.com/amore019\" class=\"former\">Marskay</a> " +
                "你过得好不好别人未必知道，你一胖别人就看出来了。" +
                "#<a href=\"/q/%E6%8A%84%E5%8F%B0%E8%AF%8D\">抄台词</a>#";
        str = filter(str);
        System.out.println(str);
    }


    /**
     * 按照 yyyy-MM-dd 日期获取每日饭否精选
     *
     * @param nowDate
     * @return
     */
    public static ResponsePo getFanFouDailyByDate(String nowDate) {
        String json = Requests.get("http://blog.fanfou.com/digest/json/" + nowDate + ".daily.json").send().readToText();
        return AppUtils.gson.fromJson(json, ResponsePo.class);
    }

    /**
     * 按照 yyyy-MM-dd 日期获取每周饭否精选
     *
     * @param nowDate
     * @return
     */
    public static ResponsePo getFanFouWeeklyByDate(String nowDate) {
        String json = Requests.get("http://blog.fanfou.com/digest/json/" + nowDate + ".weekly.json").send().readToText();
        return AppUtils.gson.fromJson(json, ResponsePo.class);
    }

    /**
     * 过滤饭否超链接和话题
     * <p>
     * 例如：
     * <p>
     * 删除 “转@<a href="http://fanfou.com/~XzdXX_fS7ZA" class="former">Tina罗</a>”
     * “#<a href="/q/%E6%8A%84%E5%8F%B0%E8%AF%8D">抄台词</a>#” 转为 #抄台词#
     *
     * @param msg
     * @return
     */
    public static String filter(String msg) {
        return msg.replaceAll("转@<a.*?</a> ", "")
                .replaceAll("<a href.*?>", "")
                .replaceAll("</a>", "")
                .replaceAll("&quot;", "\"")
                .replaceAll("&lt;", "<")
                .replaceAll("&gt;", ">")
                .replaceAll("&quot;", "\"")
                .replaceAll("&amp;", "&")
                .replaceAll("&nbsp;", " ");
    }

    /**
     * 返回原图路径
     * <p>
     * 例如：
     * http://photo2.fanfou.com/v1/mss_3d027b52ec5a4d589e68050845611e68/ff/n0/0e/dn/j6_137512.jpg@596w_1l.jpg
     * 会去除结尾的 “@596w_1l.jpg”
     *
     * @param imgUrl
     * @return
     */
    public static String convert(String imgUrl) {
        int i = imgUrl.lastIndexOf("@");
        return imgUrl.substring(0, i);
    }

    /**
     * 检查文件夹是否存在,否则创建
     *
     * @param directoryName
     */
    public static void checkFile(String directoryName) {
        File file = new File(directoryName);

        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 判断是否是 windows 系统
     *
     * @return
     */
    public static boolean isWindowsOS() {
        String property = System.getProperties().getProperty("os.name").toLowerCase();
        if (property.indexOf("windows") < 0) {
            return false;
        }
        return true;
    }

    /**
     * 根据不同的系统获取相对应的文件夹
     *
     * @return
     */
    public static String getFilePath() {
        String fileRoot = "/tmp/telegram_img/";
        if (AppUtils.isWindowsOS()) {
            fileRoot = "D:\\tmp\\telegram_img\\";
        }
        AppUtils.checkFile(fileRoot);
        return fileRoot;
    }

    /**
     * 使用 server酱 发送消息
     * http://sc.ftqq.com
     *
     * @param title 标题
     * @param body  正文
     */
    public void sendServerChan(String title, String body) {
        Map<String, String> params = new HashMap<>();
        params.put("text", title);
        params.put("desp", body);

        String response = Requests.get("https://sc.ftqq.com/" + serverChanSckey + ".send").params(params).send().readToText();
        log.info("server酱响应：{}", response);
    }

}
