package net.winterly.rxjersey.client.rxjava2;

import io.reactivex.*;
import io.reactivex.functions.Function;
import net.winterly.rxjersey.client.ClientMethodInvoker;
import org.reactivestreams.Publisher;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;
import java.util.concurrent.Future;

public class FlowableClientMethodInvoker implements ClientMethodInvoker<Object, Invocation.Builder> {

    private final HashMap<Class, Converter> converters = new HashMap<>();

    public FlowableClientMethodInvoker() {
        converters.put(Flowable.class, flowable -> flowable);
        converters.put(Observable.class, Flowable::toObservable);
        converters.put(Single.class, Flowable::singleOrError);
        converters.put(Maybe.class, Flowable::singleElement);
        converters.put(Completable.class, Flowable::ignoreElements);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, GenericType<T> responseType) {
        Future<T> future = builder.async().method(name, responseType);
        return convert(future, responseType);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        Future<T> future = builder.async().method(name, entity, responseType);
        return convert(future, responseType);
    }

    private <T> Object convert(Future<T> future, GenericType<T> responseType) {
        Function<Throwable, Publisher<? extends T>> mapError = throwable -> Flowable.error(throwable.getCause());
        Converter converter = converters.get(responseType.getRawType());

        Flowable flowable = Flowable.fromFuture(future)
                .onErrorResumeNext(mapError);

        return converter.convert(flowable);
    }

    private interface Converter<T> {
        T convert(Flowable flowable);
    }


}
