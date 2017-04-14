package net.winterly.rxjersey.client;

import net.winterly.rxjersey.client.inject.Remote;
import net.winterly.rxjersey.client.inject.RemoteResolver;
import net.winterly.rxjersey.client.inject.RxClientFactory;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Feature implementation to configure RxJava support for clients
 */
public class RxJerseyClientFeature implements Feature {

    public static final String RX_JERSEY_CLIENT_NAME = "rxJerseyClient";
    private static final TypeLiteral REMOTE_TYPE = new TypeLiteral<InjectionResolver<Remote>>() { };

    private Client client;

    public RxJerseyClientFeature register(Client client) {
        this.client = client;
        return this;
    }

    @Override
    public boolean configure(FeatureContext context) {
        context.register(RxClientExceptionMapper.class);
        context.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindFactory(RxClientFactory.class)
                        .to(RxClient.class)
                        .in(Singleton.class);

                bind(RemoteResolver.class)
                        .to(REMOTE_TYPE)
                        .in(Singleton.class);

                bind(getClient())
                        .named(RX_JERSEY_CLIENT_NAME)
                        .to(RxClient.class);
            }
        });

        return true;
    }

    private RxClient getClient() {
        if (client != null) {
            return RxObservable.from(client);
        }

        int cores = Runtime.getRuntime().availableProcessors();

        Client client = ClientBuilder.newClient();
        client.property(ClientProperties.ASYNC_THREADPOOL_SIZE, cores);

        return RxObservable.from(client);
    }
}
