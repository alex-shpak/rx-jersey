package net.winterly.rx.jersey.server;

import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.internal.LocalizationMessages;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import rx.Observable;

import javax.inject.Inject;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.container.AsyncResponse;
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
    private ServiceLocator serviceLocator;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        final AsyncContext asyncContext = suspend();
        invokeAsync(proxy, method, args, asyncContext);

        return null;
    }

    protected abstract void invokeAsync(Object proxy, Method method, Object[] args, AsyncContext asyncContext) throws Throwable;

    protected void onNext(AsyncResponse asyncResponse, Object entity) {
        asyncResponse.resume(entity);
    }

    protected void onError(AsyncResponse asyncResponse, Throwable throwable) {
        asyncResponse.resume(throwable);
    }

    private AsyncContext suspend() {
        final AsyncContext asyncContext = serviceLocator.getService(AsyncContext.class);

        if(!asyncContext.suspend()) {
            throw new ProcessingException(LocalizationMessages.ERROR_SUSPENDING_ASYNC_REQUEST());
        }

        return asyncContext;
    }
}
