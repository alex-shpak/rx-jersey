package net.winterly.rxjersey.client.rxjava;

import net.winterly.rxjersey.client.ClientMethodInvoker;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;

public class ObservableClientMethodInvoker implements ClientMethodInvoker<Object> {

    private final HashMap<Class, Converter> converters = new HashMap<>();

    public ObservableClientMethodInvoker() {
        converters.put(Observable.class, observable -> observable);
        converters.put(Single.class, Observable::toSingle);
        converters.put(Completable.class, Observable::toCompletable);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, GenericType<T> responseType) {
        return convert(builder.rx(RxObservableInvoker.class).method(name, responseType), responseType);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        return convert(builder.rx(RxObservableInvoker.class).method(name, entity, responseType), responseType);
    }

    private <T> Object convert(Observable observable, GenericType<T> responseType) {
        Converter converter = converters.get(responseType.getRawType());
        return converter.convert(observable);
    }

    private interface Converter<T> {
        T convert(Observable observable);
    }
}
