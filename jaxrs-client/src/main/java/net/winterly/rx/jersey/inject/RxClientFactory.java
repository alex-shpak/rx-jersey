package net.winterly.rx.jersey.inject;

import net.winterly.rx.jersey.RxBodyReader;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.rxjava.RxObservable;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;

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
