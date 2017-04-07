package net.winterly.rx.jersey.client.inject;

import net.winterly.rx.jersey.client.RxBodyReader;
import net.winterly.rx.jersey.client.RxJerseyClientFeature;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.util.Optional;

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
    @Named(RxJerseyClientFeature.RX_JERSEY_CLIENT_NAME)
    private Provider<Client> clientProvider;

    @Override
    public RxClient provide() {
        int poolSize = Runtime.getRuntime().availableProcessors();

        Client client = Optional.ofNullable(clientProvider.get()).orElseGet(ClientBuilder::newClient);
        client.register(RxBodyReader.class);
        client.property(ClientProperties.ASYNC_THREADPOOL_SIZE, poolSize);

        return RxObservable.from(client);
    }

    @Override
    public void dispose(RxClient instance) {
        instance.close();
    }

}
