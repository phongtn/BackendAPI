package com.wind.domain.user;

import akka.actor.AbstractActor;
import com.wind.module.akka.Actor;
import lombok.extern.slf4j.Slf4j;

@Actor
@Slf4j
public class UserActor extends AbstractActor {
    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(String.class, this::processMessage)
                .build();
    }

    private void processMessage(String message) {
        log.info("Receive message {}", message);
    }
}
