package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Completable;
import rx.Observable;
import rx.Single;

import java.lang.reflect.InvocationHandler;
import java.util.HashMap;

/**
 * Provides {@link InvocationHandler} for resources returning {@link Observable} or {@link Single}
 * and converts both to single Observable
 */
public class SingleInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    private final HashMap<Class, RxInvocationHandler<Single, ?>> handlers = new HashMap<>();

    public SingleInvocationHandlerProvider() {
        handlers.put(Observable.class, new ObservableHandler());
        handlers.put(Single.class, new SingleHandler());
        handlers.put(Completable.class, new CompletableHandler());
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        return handlers.get(invocable.getRawResponseType());
    }

    private static class ObservableHandler implements RxInvocationHandler<Single, Observable<?>> {
        @Override
        public Single convert(Observable<?> result) throws Throwable {
            return result.singleOrDefault(null).toSingle();
        }
    }

    private static class SingleHandler implements RxInvocationHandler<Single, Single<?>> {
        @Override
        public Single convert(Single<?> result) throws Throwable {
            return result;
        }
    }

    private static class CompletableHandler implements RxInvocationHandler<Single, Completable> {
        @Override
        public Single convert(Completable result) throws Throwable {
            return result.andThen(Single.just(null));
        }
    }
}
