package telegrambot.linkshortener.service;

import telegrambot.linkshortener.model.Url;

public interface UrlService {
    Url saveUrl(Url url);
}
