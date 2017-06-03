package net.winterly.rxjersey.client;

import net.winterly.rxjersey.client.inject.Remote;
import net.winterly.rxjersey.client.inject.RxClientRemoteResolver;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

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
        if (client == null) {
            client = defaultClient();
        }
        client.register(RxBodyReader.class);

        context.register(RxClientExceptionMapper.class);
        context.register(new Binder());

        return true;
    }

    private class Binder extends AbstractBinder {

        @Override
        protected void configure() {
            bind(RxClientRemoteResolver.class)
                    .to(REMOTE_TYPE)
                    .in(Singleton.class);

            if (client != null) {
                bind(client)
                        .named(RX_JERSEY_CLIENT_NAME)
                        .to(Client.class);
            }
        }
    }

    private Client defaultClient() {
        int cores = Runtime.getRuntime().availableProcessors();
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new GrizzlyConnectorProvider());
        config.property(ClientProperties.ASYNC_THREADPOOL_SIZE, cores);

        return ClientBuilder.newClient(config);
    }
}
