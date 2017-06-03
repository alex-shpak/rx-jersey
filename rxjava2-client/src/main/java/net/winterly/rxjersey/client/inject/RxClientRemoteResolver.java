package net.winterly.rxjersey.client.inject;

import net.winterly.rxjersey.client.ClientMethodInvoker;
import net.winterly.rxjersey.client.FlowableClientMethodInvoker;
import net.winterly.rxjersey.client.RxJerseyClientFeature;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.net.URI;

@Singleton
public class RxClientRemoteResolver extends RemoteResolver {

    private final FlowableClientMethodInvoker invoker = new FlowableClientMethodInvoker();

    @Inject
    @Named(RxJerseyClientFeature.RX_JERSEY_CLIENT_NAME)
    private Provider<Client> clientProvider;

    @Override
    protected WebTarget getWebTarget(URI target) {
        return clientProvider.get().target(target);
    }

    @Override
    protected ClientMethodInvoker getMethodInvoker() {
        return invoker;
    }

}
