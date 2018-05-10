package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.jersey.server.AsyncContext;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationHandler;

/**
 * Provides {@link InvocationHandler} for resources returning {@code io.reactivex.*} instances
 * and converts them to {@link Maybe}
 */
public abstract class MaybeInvocationHandler<R> extends RxInvocationHandler<Maybe<?>, R> {

    @Inject
    private Provider<ContainerRequestContext> requestContextProvider;

    @Inject
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    @Override
    protected void resume(AsyncContext asyncContext, Maybe<?> result) throws Throwable {
        final ContainerRequestContext requestContext = requestContextProvider.get();

        Completable intercept = Flowable.fromIterable(requestInterceptors)
                .flatMapCompletable(interceptor -> interceptor.intercept(requestContext));

        Maybe<Object> dispatch = Maybe.defer(() -> result);
        Maybe<Object> noContent = Maybe.defer(() -> Maybe.just(Response.noContent().build()));

        intercept.andThen(dispatch)
                .switchIfEmpty(noContent)
                .subscribe(asyncContext::resume, asyncContext::resume);
    }
}
