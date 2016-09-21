package net.winterly.rx.jersey.client;

import net.winterly.rx.jersey.client.inject.Remote;
import net.winterly.rx.jersey.client.inject.RemoteResolver;
import net.winterly.rx.jersey.client.inject.RxClientFactory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.rx.RxClient;

import javax.inject.Singleton;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Feature implementation to configure RxJava support for clients
 */
public class RxJerseyClientFeature implements Feature {

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(RxClientFactory.class).to(RxClient.class).in(Singleton.class);
                bind(RemoteResolver.class).to(new TypeLiteral<InjectionResolver<Remote>>() { }).in(Singleton.class);
            }
        });

        return true;
    }

}
