package net.winterly.rx.jersey.client.inject;

import net.winterly.rx.jersey.client.RxBodyReader;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.grizzly.connector.GrizzlyConnectorProvider;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Context;

/**
 * Factory to inject configured {@code RxClient<RxObservableInvoker>} instance. <br>
 * This factory will register {@link RxBodyReader} for provided client. <br>
 * <p>
 * Usage:
 * <pre>
 * &#064;Inject
 * RxClient rxClient;
 * </pre>
 */
public class RxClientFactory implements Factory<RxClient> {

    @Inject
    private ServiceLocator serviceLocator;

    @Context
    private ClientConfig clientConfig;

    @Override
    public RxClient provide() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.connectorProvider(new GrizzlyConnectorProvider());
        clientConfig.register(RxBodyReader.class);

        Client grizzlyClient = ClientBuilder.newBuilder().withConfig(clientConfig).build();

        return RxObservable.from(grizzlyClient);
    }

    @Override
    public void dispose(RxClient instance) {
        instance.close();
    }

}
