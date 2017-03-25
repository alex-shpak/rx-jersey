package net.winterly.rx.jersey.server;

import net.winterly.rx.jersey.server.spi.RxRequestInterceptor;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;
import rx.Observable;
import rx.Single;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public class RxMethodDispatcher implements ResourceMethodDispatcher {

    private final Response noContent = Response.noContent().build();
    private final ResourceMethodDispatcher original;

    @Context
    private javax.inject.Provider<AsyncContext> asyncContext;

    @Context
    private javax.inject.Provider<ContainerRequestContext> containerRequestContext;

    @Context
    private IterableProvider<RxRequestInterceptor> requestInterceptors;

    public RxMethodDispatcher(ResourceMethodDispatcher original) {
        this.original = original;
    }

    @Override
    public Response dispatch(Object resource, ContainerRequest request) throws ProcessingException {
        final ContainerRequestContext requestContext = containerRequestContext.get();
        final AsyncContext asyncContext = suspend();

        Single<?> intercept = Observable.from(requestInterceptors)
                .concatMap(interceptor -> interceptor.apply(requestContext))
                .lastOrDefault(null)
                .map(nullable -> null)
                .toSingle(); //will emit single null value after all observables

        Single<?> dispatch = Single.defer(() -> Single.just(original.dispatch(resource, request)))
                .map(Response::getEntity)
                .flatMap(single -> (Single<?>) single);

        intercept.flatMap(nullVal -> dispatch)
                .map(response -> response == null ? noContent : response)
                .subscribe(asyncContext::resume, asyncContext::resume);

        return null;
    }

    private AsyncContext suspend() {
        final AsyncContext context = asyncContext.get();

        if (!context.suspend()) {
            throw new ProcessingException(LocalizationMessages.ERROR_SUSPENDING_ASYNC_REQUEST());
        }

        return context;
    }
}
