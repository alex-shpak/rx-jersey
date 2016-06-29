package net.winterly.rx.jersey;

import org.glassfish.jersey.server.model.Invocable;
import org.glassfish.jersey.server.spi.internal.ResourceMethodInvocationHandlerProvider;
import rx.Observable;

import java.lang.reflect.InvocationHandler;

public class RxInvocationHandlerProvider implements ResourceMethodInvocationHandlerProvider {

    private static final InvocationHandler handler = new RxInvocationHandler();

    @Override
    public InvocationHandler create(Invocable method) {
        boolean isRx = Observable.class.isAssignableFrom(method.getRawResponseType());
        return isRx ? handler : null;
    }

}
