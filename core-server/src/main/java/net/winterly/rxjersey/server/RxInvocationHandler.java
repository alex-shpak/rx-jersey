package net.winterly.rxjersey.server;

import org.glassfish.jersey.server.AsyncContext;
import org.glassfish.jersey.server.internal.LocalizationMessages;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.ws.rs.ProcessingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * Typed invocation handler that supports result conversion
 *
 * @param <T> Method result type
 * @param <R> Required type
 */
public abstract class RxInvocationHandler<T, R> implements InvocationHandler {

    @Inject
    private Provider<AsyncContext> asyncContextProvider;

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final AsyncContext asyncContext = suspend();
        T result = convert((R) method.invoke(proxy, args));

        resume(asyncContext, result);

        return null; //async method return nulls
    }

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
     * @throws Throwable same as {@link InvocationHandler#invoke(Object, Method, Object[])}
     */
    protected abstract T convert(R result) throws Throwable;

    protected abstract void resume(AsyncContext asyncContext, T result) throws Throwable;
}
