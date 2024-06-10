package ru.otus.hw.service;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Ice;
import ru.otus.hw.domain.Steam;

import java.util.List;

@MessagingGateway
public interface HeatingGateway {

    @Gateway(requestChannel  = "iceChannel", replyChannel = "watherChannel")
    List<Steam> process(List<Ice> ices);

}
