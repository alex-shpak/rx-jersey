package net.winterly.rx.jersey.client.inject;

import net.winterly.rx.jersey.client.RxBodyReader;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;

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
        RxClient<RxObservableInvoker> rxClient = RxObservable.newClient();
        rxClient.register(RxBodyReader.class);

        return rxClient;
    }

    @Override
    public void dispose(RxClient instance) {
        instance.close();
    }
}
