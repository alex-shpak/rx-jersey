package net.winterly.rxjersey.server.rxjava;

import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;

/**
 * Provides {@link InvocationHandler} for resources returning {@link Observable} or {@link Single}
 * and converts both to single Observable
 */
public class SingleInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    private final HashMap<Class<?>, Class<? extends InvocationHandler>> handlers = new HashMap<>();

    @Inject
    private InjectionManager injectionManager;

    public SingleInvocationHandlerProvider() {
        handlers.put(Observable.class, ObservableHandler.class);
        handlers.put(Single.class, SingleHandler.class);
        handlers.put(Completable.class, CompletableHandler.class);
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        Class<?> returnType = invocable.getRawResponseType();
        if (handlers.containsKey(returnType)) {
            return injectionManager.createAndInitialize(handlers.get(returnType));
        }
        return null;
    }

    private static class ObservableHandler extends SingleInvocationHandler<Observable<?>> {
        @Override
        public Single<?> convert(Observable<?> result) {
            return result.singleOrDefault(null).toSingle();
        }
    }

    private static class SingleHandler extends SingleInvocationHandler<Single<?>> {
        @Override
        public Single<?> convert(Single<?> result) {
            return result;
        }
    }

    private static class CompletableHandler extends SingleInvocationHandler<Completable> {
        @Override
        public Single<?> convert(Completable result) {
            return result.andThen(Single.just(null));
        }
    }
}
