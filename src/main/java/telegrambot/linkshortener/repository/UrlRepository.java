package telegrambot.linkshortener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import telegrambot.linkshortener.model.Url;

import java.util.Optional;

public interface UrlRepository extends JpaRepository<Url, String> {
    Optional<Url> findLongUrlByShortUrl(String url);
}
