package net.winterly.rx.jersey.server;

import io.reactivex.*;
import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;

/**
 * Provides {@link InvocationHandler} for resources returning io.reactivex.* instances
 * and converts all to {@link Maybe}
 */
public class RxInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    private final HashMap<Class, RxInvocationHandler<Maybe, ?>> handlers = new HashMap<>();

    public RxInvocationHandlerProvider() {
        handlers.put(Flowable.class, new FlowableHandler());
        handlers.put(Observable.class, new ObservableHandler());
        handlers.put(Single.class, new SingleHandler());
        handlers.put(Completable.class, new CompletableHandler());
        handlers.put(Maybe.class, new MaybeHandler());
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        Class returnType = invocable.getRawResponseType();
        return handlers.get(returnType);
    }

    private static class FlowableHandler implements RxInvocationHandler<Maybe, Flowable<?>> {
        @Override
        public Maybe convert(Flowable<?> result) throws Throwable {
            return result.singleElement();
        }
    }

    private static class ObservableHandler implements RxInvocationHandler<Maybe, Observable<?>> {
        @Override
        public Maybe convert(Observable<?> result) throws Throwable {
            return result.singleElement();
        }
    }

    private static class SingleHandler implements RxInvocationHandler<Maybe, Single<?>> {
        @Override
        public Maybe convert(Single<?> result) throws Throwable {
            return result.toMaybe();
        }
    }

    private static class CompletableHandler implements RxInvocationHandler<Maybe, Completable> {
        @Override
        public Maybe convert(Completable result) throws Throwable {
            return result.toMaybe();
        }
    }

    private static class MaybeHandler implements RxInvocationHandler<Maybe, Maybe<?>> {
        @Override
        public Maybe convert(Maybe<?> result) throws Throwable {
            return result;
        }
    }
}
