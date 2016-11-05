package net.winterly.rx.jersey.server.invoker;

import net.winterly.rx.jersey.server.RxInvocationHandler;
import org.glassfish.jersey.server.internal.process.AsyncContext;
import rx.Single;

import java.lang.reflect.Method;

public final class SingleInvocationHandler extends RxInvocationHandler {

    @Override
    protected void invokeAsync(Object proxy, Method method, Object[] args, AsyncContext asyncContext) throws Throwable {
        Single<?> single = (Single) method.invoke(proxy, args);
        single.subscribe(
                entity -> onNext(asyncContext, entity),
                throwable -> onError(asyncContext, throwable)
        );
    }

}
