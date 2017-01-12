package net.winterly.rx.jersey.server;

import net.winterly.rx.jersey.server.filter.RxRequestInterceptor;
import org.glassfish.hk2.api.IterableProvider;
import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import rx.Observable;

import javax.inject.Provider;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
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

    @Context
    private Provider<AsyncContext> asyncContext;

    @Context
    private Provider<ContainerRequestContext> containerRequestContext;

    @Context
    private IterableProvider<RxRequestInterceptor> requestInterceptors;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final ContainerRequestContext requestContext = containerRequestContext.get();
        final AsyncContext asyncContext = suspend();

        Observable<?> interceptors = Observable.from(requestInterceptors)
                .concatMap(it -> it.filter(requestContext))
                .lastOrDefault(null);

        Observable<?> action = invokeAsync(proxy, method, args)
                .singleOrDefault(null)
                .map(response -> response == null ? Response.noContent().build() : response);

        interceptors.flatMap(it -> action).subscribe(
                asyncContext::resume,
                asyncContext::resume
        );

        return null;
    }

    protected abstract Observable<?> invokeAsync(Object proxy, Method method, Object[] args) throws Throwable;

    private AsyncContext suspend() {
        final AsyncContext context = asyncContext.get();

        if(!context.suspend()) {
            throw new ProcessingException(LocalizationMessages.ERROR_SUSPENDING_ASYNC_REQUEST());
        }

        return context;
    }
}
