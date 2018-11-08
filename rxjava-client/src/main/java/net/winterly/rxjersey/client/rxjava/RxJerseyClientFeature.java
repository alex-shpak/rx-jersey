package net.winterly.rxjersey.client.rxjava;

import net.winterly.rxjersey.client.inject.RemoteResolver;
import net.winterly.rxjersey.client.inject.RxJerseyBinder;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvokerProvider;
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
            client = createClient();
        }

        client.register(RxBodyReader.class);
        context.register(new Binder());

        return true;
    }

    private Client createClient() {
        int cores = Runtime.getRuntime().availableProcessors();
        ClientConfig config = new ClientConfig();
        config.connectorProvider(new GrizzlyConnectorProvider());
        config.property(ClientProperties.ASYNC_THREADPOOL_SIZE, cores);
        config.register(RxObservableInvokerProvider.class);

        return ClientBuilder.newClient(config);
    }

    private class Binder extends RxJerseyBinder {

        @Override
        protected void configure() {
            bind(new RemoteResolver(
                    getInjectionManager(),
                    new ObservableClientMethodInvoker(),
                    client
            ));

            bind(client).to(Client.class);
        }
    }
}
