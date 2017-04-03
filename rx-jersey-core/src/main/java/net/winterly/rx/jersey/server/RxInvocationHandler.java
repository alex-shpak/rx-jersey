package net.winterly.rx.jersey.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * Typed invocation handler that supports result conversion
 *
 * @param <T> Method result type
 * @param <R> Required type
 */
public interface RxInvocationHandler<T, R> extends InvocationHandler {

    @SuppressWarnings("unchecked")
    @Override
    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return convert((R) method.invoke(proxy, args));
    }

    /**
     * Converts method result into object of required type
     *
     * @param result method call result
     * @return converted value
     * @throws Throwable same as {@link InvocationHandler#invoke(Object, Method, Object[])}
     */
    T convert(R result) throws Throwable;

}
