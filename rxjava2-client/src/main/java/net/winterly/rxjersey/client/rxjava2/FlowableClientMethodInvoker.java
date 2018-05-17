package net.winterly.rxjersey.client.rxjava2;

import io.reactivex.*;
import net.winterly.rxjersey.client.ClientMethodInvoker;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvoker;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import java.util.HashMap;

public class FlowableClientMethodInvoker implements ClientMethodInvoker<Object> {

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
        Flowable<T> flowable = builder.rx(RxFlowableInvoker.class).method(name, responseType);
        return convert(flowable, responseType);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        Flowable<T> flowable = builder.rx(RxFlowableInvoker.class).method(name, entity, responseType);
        return convert(flowable, responseType);
    }

    private <T> Object convert(Flowable<T> flowable, GenericType<T> responseType) {
        Converter converter = converters.get(responseType.getRawType());
        return converter.convert(flowable);
    }

    private interface Converter<T> {
        T convert(Flowable flowable);
    }


}
