package ru.otus.hw.flow;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.PollableChannel;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import ru.otus.hw.Application;
import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;
import ru.otus.hw.service.HeatingGateway;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ContextConfiguration
@SpringBootTest(classes = Application.class)
@DirtiesContext
public class IntegrationFlowTests {

    public static final int ICE_TEMPERATURE_TEN = -10;
    public static final int BOILING_POINT = 100;
    @Autowired
    private HeatingGateway heatingGateway;

    @Autowired
    @Qualifier("iceChannel")
    private MessageChannel iceChannel;

    @Test
    public void testGateway() {
        var response = heatingGateway.process(List.of(new Ice(ICE_TEMPERATURE_TEN)));
        var steam = response.get(0);
        assertNotNull(response);
        assertThat(steam, Matchers.instanceOf(Steam.class));
        assertThat(steam.temperature(), Matchers.greaterThanOrEqualTo(BOILING_POINT));
    }

    @Test
    public void testChannel() {
        PollableChannel iceChannel = new QueueChannel();
        Message<Ice> message = MessageBuilder.withPayload(new Ice(ICE_TEMPERATURE_TEN)).setReplyChannel(iceChannel).build();

        this.iceChannel.send(message);

        Message<?> receive = iceChannel.receive(2000);
        assertNotNull(receive);
        assertThat(receive.getPayload(), Matchers.isA(List.class));
        var steams = (ArrayList) receive.getPayload();
        assertThat(steams.get(0), Matchers.isA(Steam.class));
        var el =(Steam) steams.get(0);
        assertThat(el.temperature(), Matchers.greaterThanOrEqualTo(BOILING_POINT));
    }
}
