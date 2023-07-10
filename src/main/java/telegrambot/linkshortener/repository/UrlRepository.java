package telegrambot.linkshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegrambot.linkshortener.model.Url;

public interface UrlRepository extends JpaRepository<Url, String> {
}
