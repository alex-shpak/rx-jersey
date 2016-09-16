package net.winterly.rx.jersey;

import net.winterly.rx.jersey.inject.RxClientFactory;
import net.winterly.rx.jersey.inject.RxWebTargetFactory;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.RxWebTarget;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

public class RxJerseyClientFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(RxBodyReader.class);
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(RxClientFactory.class).to(RxClient.class).in(Singleton.class);
                bindFactory(RxWebTargetFactory.class).to(RxWebTarget.class);
            }
        });

        return true;
    }

}
