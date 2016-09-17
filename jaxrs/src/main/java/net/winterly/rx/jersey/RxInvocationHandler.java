package net.winterly.rx.jersey;

import rx.Observable;

import javax.ws.rs.container.AsyncResponse;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

public class RxInvocationHandler implements InvocationHandler {

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Optional<AsyncResponse> asyncResponse = findAsyncResponse(args);

        if(asyncResponse.isPresent()) {
            //TODO: add async filters
            Observable<?> observable = (Observable) method.invoke(proxy, args);
            observable.subscribe(
                entity -> onNext(asyncResponse.get(), entity),
                throwable -> onError(asyncResponse.get(), throwable)
            );
        } else {
            return method.invoke(proxy, args); //default invocation
        }

        return null;
    }

    private void onNext(AsyncResponse asyncResponse, Object entity) {
        asyncResponse.resume(entity);
    }

    private void onError(AsyncResponse asyncResponse, Throwable throwable) {
        asyncResponse.resume(throwable);
    }

    private Optional<AsyncResponse> findAsyncResponse(Object[] args) {
        for (Object arg : args) {
            if(AsyncResponse.class.isAssignableFrom(arg.getClass()))
                return Optional.of((AsyncResponse) arg);
        }
        return Optional.empty();
    }
}
