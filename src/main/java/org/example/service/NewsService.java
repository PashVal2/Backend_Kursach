package org.example.service;

import org.example.model.News;
import org.example.repos.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsService {
    @Autowired
    private final NewsRepository newsRepository;

    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void addNews(String title, String url, String tag) {
        News news = new News();
        news.setTitle(title);
        news.setUrl(url);
         news.setTag(tag);

        newsRepository.save(news);
    }
    public boolean isExist(String title) {
        return newsRepository.findByTitle(title).isPresent();
    }
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
}
