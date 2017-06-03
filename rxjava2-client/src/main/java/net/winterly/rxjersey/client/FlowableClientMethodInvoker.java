package net.winterly.rxjersey.client;

import io.reactivex.Flowable;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

public class FlowableClientMethodInvoker implements ClientMethodInvoker<Flowable, Invocation.Builder> {

    @Override
    public <T> Flowable method(Invocation.Builder builder, String name, GenericType<T> responseType) {
        return Flowable.fromFuture(builder.async().method(name, responseType));
    }

    @Override
    public <T> Flowable method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        return Flowable.fromFuture(builder.async().method(name, entity, responseType));
    }
}
