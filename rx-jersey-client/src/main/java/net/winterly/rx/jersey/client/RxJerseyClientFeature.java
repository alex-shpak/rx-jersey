package net.winterly.rx.jersey.client;

import net.winterly.rx.jersey.client.inject.Remote;
import net.winterly.rx.jersey.client.inject.RemoteResolver;
import net.winterly.rx.jersey.client.inject.RxClientFactory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.rx.RxClient;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Feature implementation to configure RxJava support for clients
 */
public class RxJerseyClientFeature implements Feature {

    public static final String RX_JERSEY_CLIENT_NAME = "rxJerseyClient";
    private static final TypeLiteral REMOTE_TYPE = new TypeLiteral<InjectionResolver<Remote>>() {};

    private Client client;

    public void register(Client client) {
        this.client = client;
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(new Binder());
        return true;
    }

    private class Binder extends AbstractBinder {
        @Override
        protected void configure() {
            bindFactory(RxClientFactory.class).to(RxClient.class).in(Singleton.class);
            bind(RemoteResolver.class).to(REMOTE_TYPE).in(Singleton.class);

            if (client != null) {
                bind(client).named(RX_JERSEY_CLIENT_NAME).to(Client.class);
            }
        }
    }
}
