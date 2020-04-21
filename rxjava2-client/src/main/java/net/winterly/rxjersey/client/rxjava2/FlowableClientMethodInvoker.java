package net.winterly.rxjersey.client.rxjava2;

import io.reactivex.*;
import net.winterly.rxjersey.client.ClientMethodInvoker;
import org.glassfish.jersey.client.rx.rxjava2.RxFlowableInvoker;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.GenericType;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.function.Function;

public class FlowableClientMethodInvoker implements ClientMethodInvoker<Object> {

    private final HashMap<Class, Function<Flowable, ?>> converters = new HashMap<>();

    public FlowableClientMethodInvoker() {
        converters.put(Flowable.class, flowable -> flowable);
        converters.put(Observable.class, Flowable::toObservable);
        converters.put(Single.class, Flowable::singleOrError);
        converters.put(Maybe.class, Flowable::singleElement);
        converters.put(Completable.class, Flowable::ignoreElements);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, GenericType<T> responseType) {
        GenericType<?> responseValueType = getValueTypeIfPossible(responseType);
        Flowable<?> flowable = builder.rx(RxFlowableInvoker.class).method(name, responseValueType);
        return convert(flowable, responseType);
    }

    @Override
    public <T> Object method(Invocation.Builder builder, String name, Entity<?> entity, GenericType<T> responseType) {
        GenericType<?> responseValueType = getValueTypeIfPossible(responseType);
        Flowable<?> flowable = builder.rx(RxFlowableInvoker.class).method(name, entity, responseValueType);
        return convert(flowable, responseType);
    }

    private <T> Object convert(Flowable<?> flowable, GenericType<T> responseType) {
        Function<Flowable, ?> converter = converters.get(responseType.getRawType());
        return converter.apply(flowable);
    }

    /**
     * Jersey has special handling when the invoker response type is javax.ws.rs.core.Response
     * To maintain that behavior, use the type of the values in the Rx container (in case it is e.g. Single<Response>)
     * @param responseType
     * @param <T>
     * @return
     */
    private <T> GenericType getValueTypeIfPossible(GenericType<T> responseType) {
        if (isConvertibleParameterizedType(responseType)) {
            return getContainedType((ParameterizedType) responseType.getType());
        }
        return responseType;
    }

    private <T> boolean isConvertibleParameterizedType(GenericType<T> type) {
        return converters.containsKey(type.getRawType()) && type.getType() instanceof ParameterizedType;
    }

    private GenericType getContainedType(ParameterizedType type) {
        return new GenericType(type.getActualTypeArguments()[0]);
    }
}
