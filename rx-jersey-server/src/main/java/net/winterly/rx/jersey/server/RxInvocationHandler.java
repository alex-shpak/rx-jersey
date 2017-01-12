package net.winterly.rx.jersey.server;

import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import rx.Observable;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Invocation handler that bridges {@link Observable} and {@link AsyncResponse} for resources.
 * Handler tries to obtain {@link AsyncContext} and suspend current thread, throws exception otherwise.
 *
 * @see RxInvocationHandlerProvider
 */
public abstract class RxInvocationHandler implements InvocationHandler {

    @Inject
    private Provider<AsyncContext> asyncContext;

    @Inject
    private Provider<ContainerRequestContext> containerRequestContext;

    @Inject
    private Provider<ContainerResponseContext> containerResponseContext;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final AsyncContext asyncContext = suspend();

        invokeAsync(proxy, method, args, asyncContext)
                .singleOrDefault(null)
                .map(response -> response == null ? Response.noContent().build() : response)
                .subscribe(
                    asyncContext::resume,
                    asyncContext::resume
                );

        return null;
    }

    protected abstract Observable<?> invokeAsync(Object proxy, Method method, Object[] args, AsyncContext asyncContext) throws Throwable;

    private AsyncContext suspend() {
        final AsyncContext context = asyncContext.get();

        if(!context.suspend()) {
            throw new ProcessingException(LocalizationMessages.ERROR_SUSPENDING_ASYNC_REQUEST());
        }

        return context;
    }
}
