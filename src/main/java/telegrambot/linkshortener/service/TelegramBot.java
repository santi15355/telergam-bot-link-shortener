package telegrambot.linkshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.linkshortener.config.BotConfig;
import telegrambot.linkshortener.controller.UrlController;
import telegrambot.linkshortener.model.Url;
import telegrambot.linkshortener.model.User;
import telegrambot.linkshortener.repository.UrlRepository;
import telegrambot.linkshortener.repository.UserRepository;
import telegrambot.linkshortener.utils.ShortUrlGenerator;
import telegrambot.linkshortener.utils.UrlChecker;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")

public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UrlController urlController;
    private final UrlChecker urlChecker;
    private final ShortUrlGenerator shortUrlGenerator;

    @Autowired
    private final UserService userService;
    @Autowired
    private final UrlService urlService;
    @Autowired
    private final UrlRepository urlRepository;
    @Autowired
    private final UserRepository userRepository;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String command = update.getMessage().getText();

            if (command.equals("/start")) {
                sendMessage(chatId, "Отправь мне ссылку, которую нужно сократить");
            } else {
                String longUrlForCheck = update.getMessage().getText();
                if (urlChecker.checkUrl(longUrlForCheck)) {
                    String checkedUrl = longUrlForCheck;
                    saveData(checkedUrl, update);
                } else {
                    sendMessage(chatId, "Некоректный УРЛ");
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    private void saveData(String checkedUrl, Update update) {

        Long chatId = update.getMessage().getChatId();

        if (userRepository.findByChatId(chatId).isEmpty()) {
            User user = new User();
            List<Url> urls = new ArrayList<>();

            user.setChatId(update.getMessage().getChatId());
            user.setUserName(update.getMessage().getChat().getUserName());
            user.setUrls(urls);

            Url url = new Url();
            url.setLongUrl(checkedUrl);
            String shortLink = shortUrlGenerator.generateShortLink();
            url.setShortUrl(shortLink);
            urls.add(url);
            urlService.saveUrl(url);
            userService.saveUser(user);
            sendMessage(chatId, "Сокращенная ссылка:" + " " + "http://shortit4me.fun/" + shortLink);

        } else {
            User currentUser = userRepository.findByChatId(chatId).get();
            List<Url> userUrls = currentUser.getUrls();
            Url newUserUrl = new Url();
            newUserUrl.setLongUrl(checkedUrl);
            String shortLink = shortUrlGenerator.generateShortLink();
            newUserUrl.setShortUrl(shortLink);
            userUrls.add(newUserUrl);
            urlService.saveUrl(newUserUrl);
            userService.saveUser(currentUser);
            sendMessage(chatId, "Сокращенная ссылка:" + " " + "http://shortit4me.fun/" + shortLink);
        }
    }
}
