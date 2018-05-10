package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.jersey.server.AsyncContext;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;

public abstract class SingleInvocationHandler<R> extends RxInvocationHandler<Single<?>, R> {

    @Inject
    private Provider<ContainerRequestContext> requestContextProvider;

    @Inject
    private IterableProvider<ObservableRequestInterceptor<?>> requestInterceptors;

    @Override
    public void resume(AsyncContext asyncContext, Single<?> result) throws Throwable {
        final ContainerRequestContext requestContext = requestContextProvider.get();

        Completable intercept = Observable.from(requestInterceptors)
                .concatMap(interceptor -> interceptor.intercept(requestContext))
                .lastOrDefault(null)
                .toCompletable();

        intercept.andThen(result)
                .map(response -> response == null ? Response.noContent().build() : response)
                .subscribe(asyncContext::resume, asyncContext::resume);
    }
}
