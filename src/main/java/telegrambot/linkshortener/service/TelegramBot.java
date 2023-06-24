package telegrambot.linkshortener.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import telegrambot.linkshortener.config.BotConfig;
import telegrambot.linkshortener.model.User;

@Component
@RequiredArgsConstructor
@PropertySource("classpath:application.yml")

public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    @Autowired
    private UserService userService;

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String command = update.getMessage().getText();

            if (command.equals("/start")) {
                sendMessage(chatId, "Отправь мне ссылку, которую нужно сократить");
            } else if (command.equals("/save")) {
                User user = new User();
                user.setUserName(update.getMessage().getChat().getUserName());
                user.setChatId(chatId);
                userService.saveUser(user);
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
}