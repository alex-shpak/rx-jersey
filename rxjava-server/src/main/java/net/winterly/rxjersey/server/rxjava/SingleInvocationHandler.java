package net.winterly.rxjersey.server.rxjava;

import net.winterly.rxjersey.server.RxInvocationHandler;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.jersey.server.AsyncContext;
import rx.Completable;
import rx.Observable;
import rx.Single;

import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Response;
import java.lang.reflect.Method;

public abstract class SingleInvocationHandler<R> extends RxInvocationHandler<Single<?>, Completable, R> {

    @Inject
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) {
        final AsyncContext asyncContext = suspend();
        final ContainerRequestContext requestContext = requestContextProvider.get();

        Completable intercept = Observable.from(requestInterceptors)
                .flatMapCompletable(interceptor -> interceptor.intercept(requestContext))
                .lastOrDefault(null)
                .toCompletable();

        final Single<?> invoke = Single.defer(() -> convert((R) method.invoke(proxy, args)));

        intercept.andThen(invoke)
                .map(response -> response == null ? Response.noContent().build() : response)
                .subscribe(asyncContext::resume, asyncContext::resume);

        return null; //async method return nulls
    }
}
