package net.winterly.rx.jersey.server;

import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import org.glassfish.jersey.server.spi.internal.ResourceMethodDispatcher;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

/**
 * Generic {@link ResourceMethodDispatcher} that obtains {@link AsyncContext} and asynchronously dispatches request
 */
public abstract class RxMethodDispatcher implements ResourceMethodDispatcher {

    private final ResourceMethodDispatcher originalDispatcher;

    @Context
    private javax.inject.Provider<AsyncContext> asyncContext;

    /**
     * @param originalDispatcher original {@link ResourceMethodDispatcher} that was supposed to be invoked
     */
    public RxMethodDispatcher(ResourceMethodDispatcher originalDispatcher) {
        this.originalDispatcher = originalDispatcher;
    }

    /**
     * Uses {@link AsyncContext} to suspend current request
     *
     * @return obtained {@link AsyncContext} or throws error
     */
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
        dispatch(asyncContext, originalDispatcher, resource, request);
        return null; //should return null for dispatch requests
    }

    /**
     * Should execute interceptors, dispatch and resume with {@link AsyncContext}
     *
     * @param asyncContext Async Context
     * @param dispatcher   original {@link ResourceMethodDispatcher}
     * @param resource     the resource class instance.
     * @param request      request to be dispatched.
     * @throws ProcessingException same as {@link ResourceMethodDispatcher#dispatch(Object, ContainerRequest)}
     */
    public abstract void dispatch(AsyncContext asyncContext, ResourceMethodDispatcher dispatcher, Object resource, ContainerRequest request) throws ProcessingException;

}
