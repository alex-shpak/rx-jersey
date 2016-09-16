package net.winterly.rx.jersey.inject;

import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InstantiationService;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;

import javax.inject.Inject;
import javax.inject.Named;

public class RxWebTargetFactory implements Factory<RxWebTarget> {

    @Inject RxClient rxClient;
    @Inject InstantiationService instantiationService;

    @Override
    public RxWebTarget provide() {
        Injectee injectee = instantiationService.getInstantiationData().getParentInjectee();
        Named named = injectee.getParent().getAnnotation(Named.class);

        if(named == null) {
            throw new IllegalStateException("@java.inject.Named annotation is required for RxWebTarget");
        }

        return rxClient.target(named.value());
    }

    @Override
    public void dispose(RxWebTarget instance) {

    }
}
