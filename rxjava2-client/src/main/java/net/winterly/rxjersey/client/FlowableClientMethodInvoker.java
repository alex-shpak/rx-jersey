package net.winterly.rxjersey.client;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;

public class FlowableClientMethodInvoker implements ClientMethodInvoker<Flowable, Invocation.Builder> {

    @Override
    public <T> Flowable method(Invocation.Builder builder, String name, GenericType<T> responseType) {
        return Flowable.fromFuture(builder.async().method(name, responseType))
                .onErrorResumeNext((Function<Throwable, Publisher<? extends T>>) this::error);
    }

    @Override
    public <T> Flowable method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        return Flowable.fromFuture(builder.async().method(name, entity, responseType))
                .onErrorResumeNext((Function<Throwable, Publisher<? extends T>>) this::error);
    }

    private <T> Flowable<T> error(Throwable throwable) {
        return Flowable.error(throwable.getCause()); //execution exception to response exception
    }
}
