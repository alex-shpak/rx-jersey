package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.ServiceLocator;
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

    private final HashMap<Class, RxInvocationHandler<Single<?>, ?>> handlers = new HashMap<>();

    @Inject
    public SingleInvocationHandlerProvider(ServiceLocator serviceLocator) {
        handlers.put(Observable.class, serviceLocator.createAndInitialize(ObservableHandler.class));
        handlers.put(Single.class, serviceLocator.createAndInitialize(SingleHandler.class));
        handlers.put(Completable.class, serviceLocator.createAndInitialize(CompletableHandler.class));
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        return handlers.get(invocable.getRawResponseType());
    }

    private static class ObservableHandler extends SingleInvocationHandler<Observable<?>> {
        @Override
        public Single convert(Observable<?> result) throws Throwable {
            return result.singleOrDefault(null).toSingle();
        }
    }

    private static class SingleHandler extends SingleInvocationHandler<Single<?>> {
        @Override
        public Single convert(Single<?> result) throws Throwable {
            return result;
        }
    }

    private static class CompletableHandler extends SingleInvocationHandler<Completable> {
        @Override
        public Single convert(Completable result) throws Throwable {
            return result.andThen(Single.just(null));
        }
    }
}
