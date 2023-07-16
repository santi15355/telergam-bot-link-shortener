package telegrambot.linkshortener.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import telegrambot.linkshortener.repository.UrlRepository;

@RestController
@RequestMapping("/")
@AllArgsConstructor
public class UrlController {

    private final UrlRepository urlRepository;
    public static final String SHORT_LINK = "{shortLink}";

    @GetMapping(SHORT_LINK)
    @SneakyThrows
    public RedirectView getLongLink(@PathVariable final String shortLink) {
        String longUrl = "";
        try {
            longUrl = urlRepository.findLongUrlByShortUrl(shortLink).get().getLongUrl();
        } catch (Exception e) {}

        return new RedirectView(longUrl);
    }

}
