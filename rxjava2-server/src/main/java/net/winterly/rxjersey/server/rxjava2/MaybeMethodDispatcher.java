package net.winterly.rxjersey.server.rxjava2;

import io.reactivex.*;
import net.winterly.rxjersey.server.RxMethodDispatcher;
import net.winterly.rxjersey.server.RxMethodDispatcherProvider;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class MaybeMethodDispatcher extends RxMethodDispatcher {

    @Context
    private javax.inject.Provider<ContainerRequestContext> containerRequestContext;

    @Context
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    public MaybeMethodDispatcher(ResourceMethodDispatcher originalDispatcher) {
        super(originalDispatcher);
    }

    @Override
    public void dispatch(AsyncContext asyncContext, ResourceMethodDispatcher dispatcher, Object resource, ContainerRequest request) throws ProcessingException {
        final ContainerRequestContext requestContext = containerRequestContext.get();

        Completable intercept = Flowable.fromIterable(requestInterceptors)
                .flatMapCompletable(interceptor -> interceptor.intercept(requestContext));

        Maybe<Object> dispatch = Maybe.defer(() -> (Maybe<?>) dispatcher.dispatch(resource, request).getEntity());
        Maybe<Object> noContent = Maybe.defer(() -> Maybe.just(Response.noContent().build()));

        intercept.andThen(dispatch)
                .switchIfEmpty(noContent)
                .subscribe(asyncContext::resume, asyncContext::resume);
    }

    public static class Provider extends RxMethodDispatcherProvider {

        @Inject
        public Provider(ServiceLocator serviceLocator) {
            super(serviceLocator, Flowable.class, Observable.class, Completable.class, Single.class, Maybe.class);
        }

        @Override
        public RxMethodDispatcher create(ResourceMethodDispatcher dispatcher) {
            return new MaybeMethodDispatcher(dispatcher);
        }
    }
}
