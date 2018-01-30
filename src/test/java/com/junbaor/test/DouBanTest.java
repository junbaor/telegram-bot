package com.junbaor.test;

import com.google.gson.Gson;
import com.junbaor.test.model.DouBanResponsePo;
import net.dongliu.requests.Requests;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DouBanTest {

    static Gson gson = new Gson();

    public static void main(String[] args) throws IOException {
        String url = "https://frodo.douban.com/api/v2/selection/theme/17/items?start=0&count=5&channel=Yingyongbao_Market&os_rom=android&apikey=0dad551ec0f84ed02907ff5c42e8ec70&udid=da56cbca71e5ce7ac7f5d0da880c3ab70fbc0bfd";
        Map<String, Object> map = new HashMap<>();
        map.put("User-Agent", "api-client/1 com.douban.frodo/5.18.0(121) Android/19 product/R11 vendor/OPPO model/OPPO R11  rom/android  network/wifi");

        String text = Requests.get(url).headers(map).send().readToText();

        DouBanResponsePo douBanResponse = gson.fromJson(text, DouBanResponsePo.class);

        List<String> urlList = new ArrayList<>();
        for (DouBanResponsePo.ItemsBean itemsBean : douBanResponse.getItems()) {
            String dayUrl = itemsBean.getTarget().getUrl();
            urlList.add(dayUrl);
        }

        for (String s : urlList) {
            System.out.println("################## start");
            System.out.println(s);
            test2(s);
            System.out.println("################## end");
        }
    }

    public static void test2(String url) throws IOException {
        Document rootDocument = Jsoup.connect(url).timeout(10000).get();
        Elements select = rootDocument.select(".status-link");
        for (Element linkElement : select) {
            String linkUrl = linkElement.attr("href");
            System.out.println("------- start");
            System.out.println(linkUrl);
            test3(linkUrl);
            System.out.println("------- end");
        }
    }

    public static void test3(String url) throws IOException {
        Document rootDocument = Jsoup.connect(url).timeout(10000).get();
        String text = rootDocument.select("blockquote").text();
        System.out.println(text);

        Elements elements = rootDocument.select(".view-large");

        for (Element element : elements) {
            String href = element.attr("href");
            System.out.println(href);
        }

        System.out.println();
    }
}