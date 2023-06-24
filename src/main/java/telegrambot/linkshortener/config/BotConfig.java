package telegrambot.linkshortener.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@PropertySource("classpath:application.yml")

public class BotConfig {
    @Value("${name}")
    String name;

    @Value("${token}")
    String token;
}
