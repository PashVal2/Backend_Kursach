package org.example.job;

import org.example.service.NewsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ParseTask {
    @Autowired
    NewsService newsService;
    @Scheduled(fixedDelay = 3600000)
    public void parseNewNews() { // парсер новостей по html разметке
        String url = "https://realty.rbc.ru/";
        try {
            Document document = Jsoup.connect(url)
                    .userAgent("Mozilla")
                    .timeout(5000)
                    .referrer("https://ya.ru")
                    .get();
            Elements news = document.getElementsByClass("js-rm-central-column-item");
            for (Element element: news) {
                String title = element.select(".normal-wrap").text();
                String elUrl = element.select(".item__link").attr("href");
                String tag = element.select(".item__category").text();
                if (!newsService.isExist(title)) {
                    newsService.addNews(title, elUrl, tag);
                }
            }
            System.out.println("newRec");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
