package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import javax.inject.Inject;
import org.glassfish.jersey.internal.inject.InjectionManager;
import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;

/**
 * Provides {@link InvocationHandler} for resources returning {@code io.reactivex.*} instances
 * and converts them to {@link Maybe}
 */
class MaybeInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    private final HashMap<Class<?>, Class<? extends MaybeInvocationHandler<?>>> handlers = new HashMap<>();

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
        return createInner(invocable, invocable.getRawResponseType(),  (Class<?>) actual(invocable.getResponseType()));
    }

    private <T, U> InvocationHandler createInner(Invocable invocable, Class<T> returnType, Class<U> innerType) {
        Streamable streamable = invocable.getHandlingMethod().getAnnotation(Streamable.class);
        if (streamable == null) {
            if (handlers.containsKey(returnType)) {
                return injectionManager.createAndInitialize(handlers.get(returnType));
            }
        } else {
            if (returnType.equals(Flowable.class)) {
                return createFlowableHandler(streamable);
            }
        }
        return null;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <U> InvocationHandler createFlowableHandler(Streamable streamable) {
        return injectionManager.createAndInitialize(StreamableInvocationHandler.class)
            .setStreamWriterSupplier(() ->
                injectionManager.createAndInitialize(streamable.writer()));
    }

    private static Type actual(Type genericType) {
        if (!(genericType instanceof ParameterizedType)) {
            return genericType;
        }
        final ParameterizedType actualGenericType = (ParameterizedType) genericType;
        return actualGenericType.getActualTypeArguments()[0];
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
