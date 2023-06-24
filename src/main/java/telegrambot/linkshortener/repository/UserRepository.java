package telegrambot.linkshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegrambot.linkshortener.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
}
