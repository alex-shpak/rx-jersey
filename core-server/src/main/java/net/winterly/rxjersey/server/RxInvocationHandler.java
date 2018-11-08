package net.winterly.rxjersey.server;

import org.glassfish.jersey.server.AsyncContext;
import org.glassfish.jersey.server.internal.LocalizationMessages;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.ContainerRequestContext;
import java.lang.reflect.InvocationHandler;


/**
 * Typed invocation handler that supports result conversion
 *
 * @param <T> Method result type
 * @param <I> Interceptor result type
 * @param <R> Handler type
 */
public abstract class RxInvocationHandler<T, I, R> implements InvocationHandler {

    @Inject
    protected Provider<AsyncContext> asyncContextProvider;

    @Inject
    protected Provider<ContainerRequestContext> requestContextProvider;

    /**
     * Uses {@link AsyncContext} to suspend current request
     *
     * @return obtained {@link AsyncContext} or throws error
     */
    protected AsyncContext suspend() {
        final AsyncContext asyncContext = asyncContextProvider.get();

        if (!asyncContext.suspend()) {
            throw new ProcessingException(LocalizationMessages.ERROR_SUSPENDING_ASYNC_REQUEST());
        }

        return asyncContext;
    }

    /**
     * Converts method result into object of required reactive type
     *
     * @param result method call result
     * @return converted value
     */
    protected abstract T convert(R result);
}
