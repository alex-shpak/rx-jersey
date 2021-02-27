package net.winterly.rxjersey.client.rxjava2;

import net.winterly.rxjersey.client.ClientMethodInvoker;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

public class CompositeClientMethodInvoker implements ClientMethodInvoker<Object> {

    private final FlowableClientMethodInvoker flowableClientMethodInvoker;
    private final SyncClientMethodInvoker syncClientMethodInvoker;

    public CompositeClientMethodInvoker() {
        this.flowableClientMethodInvoker = new FlowableClientMethodInvoker();
        this.syncClientMethodInvoker = new SyncClientMethodInvoker();
    }

    @Override
    public <T> Object method(final Invocation.Builder builder, final String name, final GenericType<T> responseType) {
        return isSupportedByFlowableInvoker(responseType)
                ? flowableClientMethodInvoker.method(builder, name, responseType)
                : syncClientMethodInvoker.method(builder, name, responseType);
    }

    @Override
    public <T> Object method(final Invocation.Builder builder, final String name, final Entity<?> entity, final GenericType<T> responseType) {
        return isSupportedByFlowableInvoker(responseType)
                ? flowableClientMethodInvoker.method(builder, name, entity, responseType)
                : syncClientMethodInvoker.method(builder, name, entity, responseType);
    }

    private <T> boolean isSupportedByFlowableInvoker(final GenericType<T> responseType) {
        return flowableClientMethodInvoker.supportedTypes().contains(responseType.getRawType());
    }

}
