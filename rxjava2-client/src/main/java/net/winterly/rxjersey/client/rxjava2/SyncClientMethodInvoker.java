package net.winterly.rxjersey.client.rxjava2;

import net.winterly.rxjersey.client.ClientMethodInvoker;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

public class SyncClientMethodInvoker implements ClientMethodInvoker<Object> {

    @Override
    public <T> Object method(final Invocation.Builder builder, final String name, final GenericType<T> responseType) {
        return builder.method(name, responseType);
    }

    @Override
    public <T> Object method(final Invocation.Builder builder, final String name, final Entity<?> entity, final GenericType<T> responseType) {
        return builder.method(name, entity, responseType);
    }
}
