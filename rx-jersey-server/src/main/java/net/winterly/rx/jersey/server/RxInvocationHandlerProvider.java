package net.winterly.rx.jersey.server;

import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Observable;
import rx.Single;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Provides {@link InvocationHandler} for resources returning {@link Observable} or {@link Single}
 * and converts both to single Observable
 */
public class RxInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    @Override
    public InvocationHandler create(Invocable invocable) {
        Class returnType = invocable.getRawResponseType();

        if (Observable.class.isAssignableFrom(returnType)) {
            return (RxInvocationHandler) (proxy, method, args) ->
                    (Observable) method.invoke(proxy, args);

        } else if (Single.class.isAssignableFrom(returnType)) {
            return (RxInvocationHandler) (proxy, method, args) ->
                    ((Single) method.invoke(proxy, args)).toObservable();
        }

        return null;
    }

    public interface RxInvocationHandler extends InvocationHandler {

        @Override
        default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return invokeAsync(proxy, method, args).singleOrDefault(null);
        }

        Observable<?> invokeAsync(Object proxy, Method method, Object[] args) throws Throwable;

    }

}
