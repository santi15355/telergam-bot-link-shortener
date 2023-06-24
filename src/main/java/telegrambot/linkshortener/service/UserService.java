package telegrambot.linkshortener.service;

import telegrambot.linkshortener.model.User;

public interface UserService {
    User saveUser(User user);
}
