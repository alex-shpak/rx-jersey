package net.winterly.rxjersey.client;

import org.glassfish.jersey.client.rx.RxInvocationBuilder;
import org.glassfish.jersey.client.rx.RxInvoker;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;

public class ObservableClientMethodInvoker implements ClientMethodInvoker<Object, RxInvocationBuilder<RxInvoker<Observable>>> {

    private final HashMap<Class, Converter> converters = new HashMap<>();

    public ObservableClientMethodInvoker() {
        converters.put(Observable.class, observable -> observable);
        converters.put(Single.class, Observable::toSingle);
        converters.put(Completable.class, Observable::toCompletable);
    }

    @Override
    public <T> Object method(RxInvocationBuilder<RxInvoker<Observable>> builder, String name, GenericType<T> responseType) {
        return convert(builder.rx().method(name, responseType), responseType);
    }

    @Override
    public <T> Object method(RxInvocationBuilder<RxInvoker<Observable>> builder, String name, Entity<?> entity, GenericType<T> responseType) {
        return convert(builder.rx().method(name, entity, responseType), responseType);
    }

    private <T> Object convert(Observable observable, GenericType<T> responseType) {
        Converter converter = converters.get(responseType.getRawType());
        return converter.convert(observable);
    }

    private interface Converter<T> {
        T convert(Observable observable);
    }
}
