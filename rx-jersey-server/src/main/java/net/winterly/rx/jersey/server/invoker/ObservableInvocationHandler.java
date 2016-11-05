package net.winterly.rx.jersey.server.invoker;

import net.winterly.rx.jersey.server.RxInvocationHandler;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import rx.Observable;

import java.lang.reflect.Method;

public final class ObservableInvocationHandler extends RxInvocationHandler {

    @Override
    protected void invokeAsync(Object proxy, Method method, Object[] args, AsyncContext asyncContext) throws Throwable {
        Observable<?> observable = (Observable) method.invoke(proxy, args);
        observable.subscribe(
                entity -> onNext(asyncContext, entity),
                throwable -> onError(asyncContext, throwable)
        );
    }

}
