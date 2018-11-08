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
import java.util.function.Function;

public class ObservableClientMethodInvoker implements ClientMethodInvoker<Object> {

    private final HashMap<Class, Function<Observable, ?>> converters = new HashMap<>();

    public ObservableClientMethodInvoker() {
        converters.put(Observable.class, observable -> observable);
        converters.put(Single.class, Observable::toSingle);
        converters.put(Completable.class, Observable::toCompletable);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, GenericType<T> responseType) {
        Observable<T> observable = builder.rx(RxObservableInvoker.class).method(name, responseType);
        return convert(observable, responseType);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        Observable<T> observable = builder.rx(RxObservableInvoker.class).method(name, entity, responseType);
        return convert(observable, responseType);
    }

    private <T> Object convert(Observable observable, GenericType<T> responseType) {
        Function<Observable, ?> converter = converters.get(responseType.getRawType());
        return converter.apply(observable);
    }
}
