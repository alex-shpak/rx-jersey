package net.winterly.rx.jersey.server;

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

    private final Response noContent = Response.noContent().build();

    @Context
    private javax.inject.Provider<ContainerRequestContext> containerRequestContext;

    @Context
    private IterableProvider<CompletableRequestInterceptor> requestInterceptors;

    public MaybeMethodDispatcher(ResourceMethodDispatcher originalDispatcher) {
        super(originalDispatcher);
    }

    @Override
    public void async(AsyncContext asyncContext, ResourceMethodDispatcher dispatcher, Object resource, ContainerRequest request) throws ProcessingException {
        final ContainerRequestContext requestContext = containerRequestContext.get();

        Completable intercept = Flowable.fromIterable(requestInterceptors)
                .flatMapCompletable(interceptor -> interceptor.intercept(requestContext));

        Maybe<Object> dispatch = Maybe.defer(() -> Maybe.just(dispatcher.dispatch(resource, request)))
                .map(Response::getEntity) //entity is Maybe
                .flatMap(maybe -> (Maybe<?>) maybe);

        intercept.andThen(dispatch)
                .defaultIfEmpty(noContent)
                .subscribe(asyncContext::resume, asyncContext::resume);
    }

    public static class Provider extends RxMethodDispatcherProvider {

        @Inject
        public Provider(ServiceLocator serviceLocator) {
            super(serviceLocator, Flowable.class, Observable.class, Completable.class, Single.class, Maybe.class);
        }

        @Override
        public ResourceMethodDispatcher create(ResourceMethodDispatcher dispatcher) {
            return new MaybeMethodDispatcher(dispatcher);
        }

    }

}
