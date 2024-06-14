package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.messaging.Message;
import org.springframework.integration.handler.LoggingHandler;
import org.springframework.integration.scheduling.PollerMetadata;
import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;
import ru.otus.hw.domain.Water;

import java.util.random.RandomGenerator;

@Configuration
public class IntegrationConfig {

    public static final int MELTING_POINT_MIN = 0;

    public static final int MELTING_POINT_MAX = 2;

    public static final int BOILING_POINT_MIN = 100;

    public static final int BOILING_POINT_MAX = 102;

    private final RandomGenerator randomGenerator = RandomGenerator.getDefault();

    @Bean
    public MessageChannelSpec<?, ?> iceChannel() {
        return MessageChannels.queue(10);
    }

    @Bean
    public MessageChannelSpec<?, ?> watherChannel() {
        return MessageChannels.publishSubscribe();
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerSpec poller() {
        return Pollers.fixedRate(BOILING_POINT_MIN).maxMessagesPerPoll(2);
    }

    @Bean
    public IntegrationFlow evaporationFlow() {
        return IntegrationFlow.from(iceChannel())
                .split()
                .<Ice, Water>transform(ice -> new Water(
                        randomGenerator.nextInt(MELTING_POINT_MIN, MELTING_POINT_MAX)
                ))
                .<Water, Steam>transform(water -> new Steam(
                        randomGenerator.nextInt(BOILING_POINT_MIN, BOILING_POINT_MAX)
                ))
                .<Steam>log(LoggingHandler.Level.INFO, "Evaporation of water Call", Message::getPayload)
                .aggregate()
                .channel(watherChannel())
                .get();
    }

}
