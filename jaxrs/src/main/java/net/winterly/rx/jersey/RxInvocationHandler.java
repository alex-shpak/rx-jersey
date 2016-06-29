package net.winterly.rx.jersey;

import rx.Observable;
import rx.schedulers.Schedulers;

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
            observable
                    .subscribeOn(Schedulers.computation())
                    .subscribe(response -> asyncResponse.get().resume(response));
        } else {
            method.invoke(proxy, args); //default invocation
        }

        return null;
    }

    private Optional<AsyncResponse> findAsyncResponse(Object[] args) {
        for (Object arg : args) {
            if(AsyncResponse.class.isAssignableFrom(arg.getClass()))
                return Optional.of((AsyncResponse) arg);
        }
        return Optional.empty();
    }
}
