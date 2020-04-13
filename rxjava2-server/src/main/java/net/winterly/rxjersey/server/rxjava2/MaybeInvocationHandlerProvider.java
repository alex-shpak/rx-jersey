package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.*;
import org.glassfish.jersey.internal.inject.InjectionManager;
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

    private final HashMap<Class<?>, Class<? extends InvocationHandler>> handlers = new HashMap<>();

    @Inject
    private InjectionManager injectionManager;

    public MaybeInvocationHandlerProvider() {
        handlers.put(Flowable.class, FlowableHandler.class);
        handlers.put(Observable.class, ObservableHandler.class);
        handlers.put(Single.class, SingleHandler.class);
        handlers.put(Completable.class, CompletableHandler.class);
        handlers.put(Maybe.class, MaybeHandler.class);
    }

    @Override
    public InvocationHandler create(Invocable invocable) {
        Class<?> returnType = invocable.getRawResponseType();
        if (handlers.containsKey(returnType)) {
            return injectionManager.createAndInitialize(handlers.get(returnType));
        }
        return null;
    }

    private static class FlowableHandler extends MaybeInvocationHandler<Flowable<?>> {
        @Override
        public Maybe<?> convert(Flowable<?> result) {
            return result.singleElement();
        }
    }

    private static class ObservableHandler extends MaybeInvocationHandler<Observable<?>> {
        @Override
        public Maybe<?> convert(Observable<?> result) {
            return result.singleElement();
        }
    }

    private static class SingleHandler extends MaybeInvocationHandler<Single<?>> {
        @Override
        public Maybe<?> convert(Single<?> result) {
            return result.toMaybe();
        }
    }

    private static class CompletableHandler extends MaybeInvocationHandler<Completable> {
        @Override
        public Maybe<?> convert(Completable result) {
            return result.toMaybe();
        }
    }

    private static class MaybeHandler extends MaybeInvocationHandler<Maybe<?>> {
        @Override
        public Maybe<?> convert(Maybe<?> result) {
            return result;
        }
    }
}
