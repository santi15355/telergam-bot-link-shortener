package telegrambot.linkshortener.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import telegrambot.linkshortener.model.Url;
import telegrambot.linkshortener.repository.UrlRepository;

@Service
public class UrlServiceImpl implements UrlService {

    @Autowired
    private UrlRepository urlRepository;

    @Override
    public Url saveUrl(Url url) {
        return urlRepository.save(url);
    }
}
