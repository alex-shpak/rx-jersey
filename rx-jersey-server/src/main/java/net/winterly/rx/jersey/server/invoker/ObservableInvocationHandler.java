package net.winterly.rx.jersey.server.invoker;

import net.winterly.rx.jersey.server.RxInvocationHandler;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import rx.Observable;

import java.lang.reflect.Method;

public final class ObservableInvocationHandler extends RxInvocationHandler {

    @Override
    protected Observable<?> invokeAsync(Object proxy, Method method, Object[] args, AsyncContext asyncContext) throws Throwable {
        return (Observable) method.invoke(proxy, args);
    }

}
