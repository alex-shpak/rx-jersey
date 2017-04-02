package net.winterly.rxjersey.server;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

public abstract class RxMethodDispatcher implements ResourceMethodDispatcher {

    private final ResourceMethodDispatcher originalDispatcher;

    @Context
    private javax.inject.Provider<AsyncContext> asyncContext;

    public RxMethodDispatcher(ResourceMethodDispatcher originalDispatcher) {
        this.originalDispatcher = originalDispatcher;
    }

    private AsyncContext suspend() {
        final AsyncContext context = asyncContext.get();

        if (!context.suspend()) {
            throw new ProcessingException(LocalizationMessages.ERROR_SUSPENDING_ASYNC_REQUEST());
        }

        return context;
    }

    @Override
    public Response dispatch(Object resource, ContainerRequest request) throws ProcessingException {
        final AsyncContext asyncContext = suspend();
        async(asyncContext, originalDispatcher, resource, request);
        return null; //should return null for async requests
    }

    public abstract void async(AsyncContext asyncContext, ResourceMethodDispatcher dispatcher, Object resource, ContainerRequest request) throws ProcessingException;

}
