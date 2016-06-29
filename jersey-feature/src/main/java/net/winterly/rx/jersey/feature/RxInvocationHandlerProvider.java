package net.winterly.rx.jersey.feature;

import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Observable;

import java.lang.reflect.InvocationHandler;

public class RxInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    @Override
    public InvocationHandler create(Invocable method) {
        if(Observable.class.isAssignableFrom(method.getRawResponseType())) {
            return new RxInvocationHandler();
        } else {
            return null;
        }
    }

}
