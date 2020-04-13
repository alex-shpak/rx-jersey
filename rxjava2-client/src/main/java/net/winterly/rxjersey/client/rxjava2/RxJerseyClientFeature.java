package net.winterly.rxjersey.client.rxjava2;

import net.winterly.rxjersey.client.inject.RemoteResolver;
import net.winterly.rxjersey.client.inject.RxJerseyBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvokerProvider;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;

/**
 * Feature implementation to configure RxJava support for clients
 */
public class RxJerseyClientFeature implements Feature {

    private Client client;

    public RxJerseyClientFeature setClient(Client client) {
        this.client = client;
        return this;
    }

    @Override
    public boolean configure(FeatureContext context) {
        if (client == null) {
            client = defaultClient();
        }

        client.register(RxBodyReader.class);
        context.register(new Binder());

        return true;
    }

    private Client defaultClient() {
        int cores = Runtime.getRuntime().availableProcessors();
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new GrizzlyConnectorProvider());
        config.property(ClientProperties.ASYNC_THREADPOOL_SIZE, cores);
        config.register(RxFlowableInvokerProvider.class);

        return ClientBuilder.newClient(config);
    }

    private class Binder extends RxJerseyBinder {

        @Override
        protected void configure() {
            bind(new RemoteResolver(
                    getInjectionManager(),
                    new FlowableClientMethodInvoker(),
                    client
            ));

            bind(client).to(Client.class);
            /*bind(create(RemoteResolver.class));

            bind(FlowableClientMethodInvoker.class)
                    .to(ClientMethodInvoker.class)
                    .in(Singleton.class);

            bind(client)
                    .named(RemoteResolver.RX_JERSEY_CLIENT_NAME)
                    .to(Client.class);*/
        }
    }
}
