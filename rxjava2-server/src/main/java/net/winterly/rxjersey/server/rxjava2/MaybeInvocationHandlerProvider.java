package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.*;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;

import javax.inject.Inject;
import java.lang.reflect.InvocationHandler;
import java.util.HashMap;

/**
 * Provides {@link InvocationHandler} for resources returning {@code io.reactivex.*} instances
 * and converts them to {@link Maybe}
 */
public class MaybeInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    private final HashMap<Class, MaybeInvocationHandler<?>> handlers = new HashMap<>();

    @Inject
    public MaybeInvocationHandlerProvider(ServiceLocator serviceLocator) {
        handlers.put(Flowable.class, serviceLocator.createAndInitialize(FlowableHandler.class));
        handlers.put(Observable.class, serviceLocator.createAndInitialize(ObservableHandler.class));
        handlers.put(Single.class, serviceLocator.createAndInitialize(SingleHandler.class));
        handlers.put(Completable.class, serviceLocator.createAndInitialize(CompletableHandler.class));
        handlers.put(Maybe.class, serviceLocator.createAndInitialize(MaybeHandler.class));
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        return handlers.get(invocable.getRawResponseType());
    }

    private static class FlowableHandler extends MaybeInvocationHandler<Flowable<?>> {
        @Override
        public Maybe convert(Flowable<?> result) throws Throwable {
            return result.singleElement();
        }
    }

    private static class ObservableHandler extends MaybeInvocationHandler<Observable<?>> {
        @Override
        public Maybe convert(Observable<?> result) throws Throwable {
            return result.singleElement();
        }
    }

    private static class SingleHandler extends MaybeInvocationHandler<Single<?>> {
        @Override
        public Maybe convert(Single<?> result) throws Throwable {
            return result.toMaybe();
        }
    }

    private static class CompletableHandler extends MaybeInvocationHandler<Completable> {
        @Override
        public Maybe convert(Completable result) throws Throwable {
            return result.toMaybe();
        }
    }

    private static class MaybeHandler extends MaybeInvocationHandler<Maybe<?>> {
        @Override
        public Maybe convert(Maybe<?> result) throws Throwable {
            return result;
        }
    }
}
