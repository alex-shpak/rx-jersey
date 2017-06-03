package net.winterly.rxjersey.client.inject;

import net.winterly.rxjersey.client.ObservableClientMethodInvoker;
import net.winterly.rxjersey.client.ClientMethodInvoker;
import org.glassfish.jersey.client.rx.RxClient;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.client.WebTarget;
import java.net.URI;

@Singleton
public class RxClientRemoteResolver extends RemoteResolver {

    private final ObservableClientMethodInvoker invoker = new ObservableClientMethodInvoker();

    @Inject
    private RxClient rxClient;

    @Override
    protected WebTarget getWebTarget(URI target) {
        return rxClient.target(target);
    }

    @Override
    protected ClientMethodInvoker getMethodInvoker() {
        return invoker;
    }

}
