package com.wind.module.akka;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class SpringActorProducer implements IndirectActorProducer {

    private final ApplicationContext applicationContext;
    private final Class<? extends Actor> actorBeanClass;
    private final Object[] parameters;

    public SpringActorProducer(ApplicationContext applicationContext,
                               Class<? extends Actor> actorBeanClass, Object[] parameters) {
        this.applicationContext = applicationContext;
        this.actorBeanClass = actorBeanClass;
        this.parameters = parameters;
    }

    public SpringActorProducer(ApplicationContext applicationContext,
                               Class<? extends Actor> actorBeanClass) {
        this.applicationContext = applicationContext;
        this.actorBeanClass = actorBeanClass;
        this.parameters = new Object[0];
    }

    @Override
    public Actor produce() {
        return applicationContext.getBean(actorBeanClass, parameters);
    }

    @Override
    public Class<? extends Actor> actorClass() {
        return actorBeanClass;
    }
}
