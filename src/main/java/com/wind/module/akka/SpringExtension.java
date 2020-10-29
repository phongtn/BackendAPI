package com.wind.module.akka;

import akka.actor.Actor;
import akka.actor.*;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringExtension extends AbstractExtensionId<SpringExtension.SpringExt> {

    private static vn.com.viettel.vds.actor.SpringExtension instance = new vn.com.viettel.vds.actor.SpringExtension();

    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static vn.com.viettel.vds.actor.SpringExtension getInstance() {
        return instance;
    }

    public static class SpringExt implements Extension {

        private static ApplicationContext applicationContext;

        public void initialize(ApplicationContext applicationContext) {
            SpringExt.applicationContext = applicationContext;
        }

        Props props(Class<? extends Actor> actorBeanClass) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanClass);
        }

        Props props(Class<? extends Actor> actorBeanClass, Object... parameters) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanClass,
                    parameters);
        }
    }
}
