package net.winterly.rxjersey.server;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface RxInvocationHandler<T, R> extends InvocationHandler {

    @SuppressWarnings("unchecked")
    @Override
    default Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return convert((R) method.invoke(proxy, args));
    }

    T convert(R result) throws Throwable;

}
