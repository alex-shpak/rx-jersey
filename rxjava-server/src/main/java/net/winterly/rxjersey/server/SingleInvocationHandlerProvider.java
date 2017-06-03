package net.winterly.rxjersey.server;

import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
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
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        Class returnType = invocable.getRawResponseType();
        return handlers.get(returnType);
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
}
