package net.winterly.rx.jersey.client.inject;

import net.winterly.rx.jersey.client.RxBodyReader;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Factory to inject {@code RxClient<RxObservableInvoker>} instance. <br>
 * This factory will register {@link RxBodyReader} for provided client. <br>
 *
 * Usage:
 * <pre>
 * &#064;Inject
 * RxClient rxClient;
 * </pre>
 */
public class RxClientFactory implements Factory<RxClient> {

    @Override
    public RxClient provide() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new GrizzlyConnectorProvider());
        clientConfig.register(RxBodyReader.class);

        Client grizzlyClient = ClientBuilder.newClient(clientConfig);
        ExecutorService executor = Executors.newFixedThreadPool(4);

        return RxObservable.from(grizzlyClient, executor);
    }

    @Override
    public void dispose(RxClient instance) {
        instance.close();
    }
}
