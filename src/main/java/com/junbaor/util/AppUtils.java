package com.junbaor.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.junbaor.App;
import com.junbaor.model.ResponsePo;
import net.dongliu.requests.Requests;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class AppUtils {

    private static final Logger log = LoggerFactory.getLogger(AppUtils.class);
    private static Properties properties = new Properties();

    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();
        System.out.println(cal.get(Calendar.DAY_OF_WEEK));

        LocalDateTime now = LocalDateTime.now();
        DayOfWeek dayOfWeek = now.plusDays(1).getDayOfWeek();
        System.out.println(dayOfWeek.getValue());

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

    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

    /**
     * 判断今天星期几
     * 1 星期日 2星期一
     *
     * @return
     */
    public static int getDayOnWeek() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获取下一次启动时间距当前毫秒数
     *
     * @return
     */
    public static Long getNextTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime target = LocalDateTime.of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 8, 5);

        if (now.isAfter(target)) {
            now = now.plusDays(1);
        }

        String nextDate = dateTimeFormatter.format(now);
        String nextDateTime = nextDate + " 08:05:00";
        Date result = null;
        try {
            result = simpleDateFormat.parse(nextDateTime);
        } catch (ParseException e) {
            log.error(e.getMessage(), e);
        }

        return result.getTime() - new Date().getTime();
    }

    /**
     * 获取饭否当天精选
     *
     * @return
     */
    public static ResponsePo getFanFouDaily() {
        String nowDate = dateTimeFormatter.format(LocalDateTime.now());
        String json = Requests.get("http://blog.fanfou.com/digest/json/" + nowDate + ".daily.json").send().readToText();
        return AppUtils.gson.fromJson(json, ResponsePo.class);
    }

    /**
     * 获取饭否当周精选 只允许周一调用
     *
     * @return
     */
    public static ResponsePo getFanFouWeekly() {
        String nowDate = dateTimeFormatter.format(LocalDateTime.now());
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
                .replaceAll("</a>", "");
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
     * 加载配置文件
     */
    public static void initConfig() {
        try {
            properties.load(App.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 获取配置
     *
     * @param name
     * @return
     */
    public static String getConfig(String name) {
        return properties.getProperty(name);
    }
}
