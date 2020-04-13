package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.jersey.server.AsyncContext;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Provides {@link InvocationHandler} for resources returning {@code io.reactivex.*} instances
 * and converts them to {@link Maybe}
 */
public abstract class MaybeInvocationHandler<R> extends RxInvocationHandler<Maybe<?>, Completable, R> {

    @Inject
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) {
        final AsyncContext asyncContext = suspend();
        final ContainerRequestContext requestContext = requestContextProvider.get();

        Completable intercept = Flowable.fromIterable(requestInterceptors)
                .flatMapCompletable(interceptor -> interceptor.intercept(requestContext));

        Maybe<Object> invoke = Maybe.defer(() -> convert((R) method.invoke(proxy, args)));
        Maybe<Object> noContent = Maybe.defer(() -> Maybe.just(Response.noContent().build()));

        intercept.andThen(invoke)
                .switchIfEmpty(noContent)
                .subscribe(asyncContext::resume, asyncContext::resume);

        return null; //async method return nulls
    }
}
