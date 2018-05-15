package net.winterly.rxjersey.client.rxjava;

import net.winterly.rxjersey.client.ClientMethodInvoker;
import net.winterly.rxjersey.client.RxClientExceptionMapper;
import net.winterly.rxjersey.client.inject.Remote;
import net.winterly.rxjersey.client.inject.RemoteResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvokerProvider;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.internal.inject.InjectionResolver;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Feature implementation to configure RxJava support for clients
 */
public class RxJerseyClientFeature implements Feature {

    private Client client;

    public RxJerseyClientFeature register(Client client) {
        this.client = client;
        return this;
    }

    @Override
    public boolean configure(FeatureContext context) {
        if (client == null) {
            client = defaultClient();
        }

        client.register(RxBodyReader.class);

        context.register(RxClientExceptionMapper.class);
        context.register(new Binder());

        return true;
    }

    private Client defaultClient() {
        int cores = Runtime.getRuntime().availableProcessors();
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new GrizzlyConnectorProvider());
        config.property(ClientProperties.ASYNC_THREADPOOL_SIZE, cores);
        config.register(RxObservableInvokerProvider.class);

        return ClientBuilder.newClient(config);
    }

    private class Binder extends org.glassfish.hk2.utilities.binding.AbstractBinder {

        @Inject
        private InjectionManager injectionManager;

        @Override
        protected void configure() {

            bind(RemoteResolver.class)
                    .to(new TypeLiteral<InjectionResolver<Remote>>() {})
                    .in(Singleton.class);

            bind(ObservableClientMethodInvoker.class)
                    .to(ClientMethodInvoker.class)
                    .in(Singleton.class);

            bind(client)
                    .named(RemoteResolver.RX_JERSEY_CLIENT_NAME)
                    .to(Client.class);
        }
    }
}
